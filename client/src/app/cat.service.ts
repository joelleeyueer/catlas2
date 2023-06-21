import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { CatList } from './model/model';

@Injectable({
  providedIn: 'root'
})
export class CatService {

  private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient) { }

  //TODO: change to get cats by location with place autocomplete
  getCats(latitude: number, longitude: number): Observable<CatList> {
    const url = `${this.apiURI}/search?long=${longitude}&lat=${latitude}`;
    return this.http.get<CatList>(url);

  }
}
