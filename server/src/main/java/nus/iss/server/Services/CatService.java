package nus.iss.server.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.server.Repositories.CatRepository;

@Service
public class CatService {

    @Autowired
    CatRepository catRepository;

    public Boolean approveCat(String catId) {
        try {
            catRepository.approveCatByCatId(catId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean rejectCat(String catId) {
        try {
            catRepository.rejectCatByCatId(catId);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
