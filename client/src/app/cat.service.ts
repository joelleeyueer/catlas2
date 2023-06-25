import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, firstValueFrom } from 'rxjs';
import { CatInfo, CatList, Fundraiser , AddCatForm} from './model/model';

@Injectable({
  providedIn: 'root'
})
export class CatService {

  private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient) { }

  getCats(address: String): Observable<CatList> {
    const url = `${this.apiURI}/search?address=${address}`;
    return this.http.get<CatList>(url);

  }

  getCatDetails(id: string): Observable<CatInfo> {
    const url = `${this.apiURI}/cat/${id}`;
    return this.http.get<CatInfo>(url);
  }

  getCatFundraiser(id: string): Observable<any> {
    const url = `${this.apiURI}/cat/${id}/fundraiser`;
    return this.http.get<Fundraiser>(url);
  }


  addCatRequest(addCatForm: AddCatForm) {
    const formData = new FormData();
    formData.append('profilePhoto', addCatForm.profilePhoto, addCatForm.profilePhoto.name);
    formData.append('locationAddress', addCatForm.locationAddress);
    formData.append('name', addCatForm.name);
    formData.append('gender', addCatForm.gender);
    formData.append('birthday', addCatForm.birthday.toString());
    formData.append('sterilization', addCatForm.sterilization.toString());
    formData.append('personalityTraits', JSON.stringify(addCatForm.personalityTraits));
    formData.append('dietLikes', JSON.stringify(addCatForm.dietLikes));
    formData.append('dietDislikes', JSON.stringify(addCatForm.dietDislikes));
    formData.append('feedingNotes', JSON.stringify(addCatForm.feedingNotes));
      
    return firstValueFrom(
      this.http.post<any>(`${this.apiURI}/newcat`, formData)
    )
    .then(response => {
        console.log('Cat added successfully');
        return response;
    })
    .catch(err => {
        console.error('Error occurred while adding cat: ' + err);
        throw err; // Re-throw the error so it can be caught by the caller
    });
}

  
}
