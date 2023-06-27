import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, firstValueFrom, throwError } from 'rxjs';
import { CatInfo, CatList, Fundraiser , AddCatForm, AddFundraiserForm} from './model/model';
import { catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CatService {

  // private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken'); // Assuming token is stored in local storage
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getCats(address: String): Observable<CatList> {
    const url = `/search?address=${address}`;
    return this.http.get<CatList>(url);

  }

  getCatDetails(id: string): Observable<CatInfo> {
    const url = `/cat/${id}`;
    return this.http.get<CatInfo>(url);
  }

  getCatDetailsAdmin(id: string): Observable<CatInfo> {
    const url = `/admin/cat/${id}`;
    const headers = this.getAuthHeaders();
    return this.http.get<CatInfo>(url, { headers });
  }
  

  getCatFundraiser(id: string): Observable<any> {
    const url = `/cat/${id}/fundraiser`;
    return this.http.get<Fundraiser>(url).pipe(catchError(this.handleError));
  }

  getCatFundraiserAdmin(id: string): Observable<any> {
    const url = `/admin/cat/${id}/fundraiser`;
    const headers = this.getAuthHeaders();
    return this.http.get<Fundraiser>(url, { headers }).pipe(catchError(this.handleError));
  }

  private handleError(error: any) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
        // client-side error
        errorMessage = `Error: ${error.error.message}`;
    } else {
        // server-side error
        errorMessage = `Error Code: ${error.status}\nMessage: ${error.error.error}`;
    }
    return throwError(errorMessage);
}


  addCatRequest(addCatForm: AddCatForm) {
    const formData = new FormData();
    formData.append('profilePhoto', addCatForm.profilePhoto, addCatForm.profilePhoto.name);
    formData.append('locationAddress', addCatForm.locationAddress);
    formData.append('name', addCatForm.name);
    formData.append('username', addCatForm.username);
    formData.append('gender', addCatForm.gender);
    formData.append('birthday', addCatForm.birthday.toString());
    formData.append('sterilization', addCatForm.sterilization.toString());

    addCatForm.personalityTraits?.forEach((trait: string) => {
      formData.append('personalityTraits', trait);
    });
  
    addCatForm.dietLikes?.forEach((like: string) => {
      formData.append('dietLikes', like);
    });
  
    addCatForm.dietDislikes?.forEach((dislike: string) => {
      formData.append('dietDislikes', dislike);
    });
  
    addCatForm.feedingNotes?.forEach((note: string) => {
      formData.append('feedingNotes', note);
    });

    const headers = this.getAuthHeaders();

      
    return firstValueFrom(
      
      this.http.post<any>(`/newcat`, formData, { headers })
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

  fundraiserRequest(fundraiserForm: AddFundraiserForm) {
    const formData = new FormData();
    formData.append('photo', fundraiserForm.photo, fundraiserForm.photo.name);
    formData.append('catId', fundraiserForm.catId);
    formData.append('username', fundraiserForm.username);
    formData.append('title', fundraiserForm.title);
    formData.append('description', fundraiserForm.description);
    formData.append('donationGoal', fundraiserForm.donationGoal.toString());
    formData.append('deadline', fundraiserForm.deadline.toString());
    const headers = this.getAuthHeaders();

    return firstValueFrom(
      this.http.post<any>(`/newfundraiser`, formData, { headers })
    )
    .then(response => {
        console.log('Fundraiser added successfully');
        return response;
    })
    .catch(err => {
        console.error('Error occurred while adding fundraiser: ' + err);
        throw err; // Re-throw the error so it can be caught by the caller
    }); 
    
  }

  
}
