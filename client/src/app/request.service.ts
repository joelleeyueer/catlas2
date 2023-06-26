import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CatRequest, CatRequests, FundraiserRequest, FundraiserRequests } from './model/model';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class RequestService {

  apiUrl = 'http://localhost:8080/admin'; // replace with your actual API root URL


  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken'); // Assuming token is stored in local storage
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  constructor(private http: HttpClient) { }

  getCatRequests(): Observable<CatRequests> {
    const headers = this.getAuthHeaders();
    return this.http.get<CatRequests>(`${this.apiUrl}/viewCatRequests`, { headers });
  }

  getFundraiserRequests(): Observable<FundraiserRequests> {
    const headers = this.getAuthHeaders();
    return this.http.get<FundraiserRequests>(`${this.apiUrl}/viewFundraiserRequests`, { headers });
  }

  approveCatRequest(catId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/approveCat/${catId}`, null, { headers });
  }

  rejectCatRequest(catId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/rejectCat/${catId}`, null, { headers });
  }

  approveFundraiserRequest(fundId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/approveFundraiser/${fundId}`, null, { headers });
  }

  rejectFundraiserRequest(fundId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/rejectFundraiser/${fundId}`, null, { headers });
  }
}
