package nus.iss.server.Services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class UploadToS3Service {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${do.spaces.region}")
    private String region;

    public String uploadSingleFile(MultipartFile incomingFile) throws IOException {
        String fileName = incomingFile.getOriginalFilename();

        if (fileName != null) {
            // Retrieve the file's bytes and create an InputStream for S3
            byte[] incomingFileBytes = incomingFile.getBytes();
            InputStream fileInputStream = new ByteArrayInputStream(incomingFileBytes);

            // Create metadata and set the content length
            ObjectMetadata currentPhotoMetadata = new ObjectMetadata(); 
            currentPhotoMetadata.setContentLength(incomingFileBytes.length);

            // Determine the content type from the file extension
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            String contentType = getContentType(fileExtension);
            
            if (contentType == null) {
                throw new IllegalArgumentException("File type not supported for " + fileName + ".");
            }

            fileName = generateUUID(fileName);
            
            currentPhotoMetadata.setContentType(contentType);
            currentPhotoMetadata.addUserMetadata("FileName", fileName);

            // Upload the file to S3
            uploadToS3(fileInputStream, currentPhotoMetadata, fileName);

            // Generate the URL and return it
            String incomingUrl = generateImageUrl(fileName);
            return incomingUrl;
        } else {
            throw new IllegalArgumentException("File name is null.");
        }
    }

    private String getContentType(String fileExtension) {
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return null;
        }
    }

    private void uploadToS3(InputStream fileBytes, ObjectMetadata currentPhotoMetadata, String fileName) {
        try {
            // Upload to S3
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileBytes, currentPhotoMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
            System.out.println("Upload to S3 successful");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private String generateImageUrl(String fileName) {
        String imageUrl = "https://" + bucketName + "." + s3Client.getRegionName() + ".digitaloceanspaces.com/" + fileName;
        System.out.println("UploadToS3Service, Image URL: " + imageUrl);
        return imageUrl;
    }

    private String generateUUID(String postfix){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString().substring(0,4) + "-" + postfix;
        return randomUUIDString;
    }

    
}
