import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { CatInfo, CatList, Fundraiser } from './model/model';

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
  
}
