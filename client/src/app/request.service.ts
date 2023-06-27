import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CatRequest, CatRequests, FundraiserRequest, FundraiserRequests } from './model/model';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class RequestService {

  // apiUrl = 'http://localhost:8080/admin';


  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken'); //token in local storage
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  constructor(private http: HttpClient) { }

  getCatRequests(): Observable<CatRequests> {
    const headers = this.getAuthHeaders();
    return this.http.get<CatRequests>(`/admin/viewCatRequests`, { headers });
  }

  getFundraiserRequests(): Observable<FundraiserRequests> {
    const headers = this.getAuthHeaders();
    return this.http.get<FundraiserRequests>(`/admin/viewFundraiserRequests`, { headers });
  }

  approveCatRequest(catId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`/admin/approveCat/${catId}`, null, { headers });
  }

  rejectCatRequest(catId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`/admin/rejectCat/${catId}`, null, { headers });
  }

  approveFundraiserRequest(fundId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`/admin/approveFundraiser/${fundId}`, null, { headers });
  }

  rejectFundraiserRequest(fundId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`/admin/rejectFundraiser/${fundId}`, null, { headers });
  }
}
