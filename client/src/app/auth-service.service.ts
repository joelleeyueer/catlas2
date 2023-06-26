import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient, private router: Router) { }

  signUp(userInfo: {name: string, password: string, email: string, roles: string}): Observable<any> {
    return this.http.post(`${this.apiURI}/auth/register`, userInfo);
  }

  login(user: {name: string, password: string}): Observable<string> {
    return this.http.post(`${this.apiURI}/auth/authenticate`, user, { responseType: 'text' });
  }

  setToken(token: string) {
    localStorage.setItem('authToken', token);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token ? true : false;
}


  logout() {
    localStorage.removeItem('authToken');
  }
}
