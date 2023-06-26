import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CatRequest, CatRequests, FundraiserRequest, FundraiserRequests } from './model/model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  apiUrl = 'http://localhost:8080/admin'; // replace with your actual API root URL

  constructor(private http: HttpClient) { }

  getCatRequests(): Observable<CatRequests> {
    return this.http.get<CatRequests>(`${this.apiUrl}/viewCatRequests`);
  }

  getFundraiserRequests(): Observable<FundraiserRequests> {
    return this.http.get<FundraiserRequests>(`${this.apiUrl}/viewFundraiserRequests`);
  }

  approveCatRequest(catId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/approveCat/${catId}`, {});
  }

  rejectCatRequest(catId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/rejectCat/${catId}`, {});
  }

  approveFundraiserRequest(fundId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/approveFundraiser/${fundId}`, {});
  }

  rejectFundraiserRequest(fundId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/rejectFundraiser/${fundId}`, {});
  }
}
