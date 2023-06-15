import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CatService {

  constructor(private http: HttpClient) { }

  sendCoordinates( latitude: number, longitude: number): Observable<any> {
    const url = `/search?long=${longitude}&lat=${latitude}`;
    return this.http.get(url);

  }
}
