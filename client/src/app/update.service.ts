import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { UpdateForm } from './model/model';

@Injectable({
  providedIn: 'root'
})
export class UpdateService {

  private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient) { }


  updateCat(updateForm: UpdateForm) {
    const formData = new FormData();
    formData.append('type', updateForm.type);
    formData.append('catId', updateForm.catId);
    formData.append('location', updateForm.location);
    formData.append('datetime', updateForm.datetime.toString());
    formData.append('comments', updateForm.comments);
    if (updateForm.photo) {
      formData.append('photo', updateForm.photo, updateForm.photo.name);
    }

    if (updateForm.type === 'fed') {
      formData.append('foodType', updateForm.foodType);
      formData.append('waterStatus', updateForm.waterStatus);
    }

    return firstValueFrom(
      this.http.post<any>(`${this.apiURI}/cat/${updateForm.catId}/updated`, formData)
    )
      .then(response => {
        console.log('Update created successfully');
        return response;
      })
      .catch(err => {
        console.error('Error occurred while creating update: ' + err);
        throw err; // Re-throw the error so it can be caught by the caller
      });
    }
}
