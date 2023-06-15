import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CatService {

  private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient) { }

  sendCoordinates( latitude: number, longitude: number): Observable<any> {
    const url = `${this.apiURI}/search?long=${longitude}&lat=${latitude}`;
    return this.http.get(url);

  }
}
