import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiURI = 'http://localhost:8080';


  constructor(private http: HttpClient, private router: Router) { }

  signUp(userInfo: {name: string, password: string, email: string, roles: string}): Observable<any> {
    return this.http.post(`${this.apiURI}/auth/register`, userInfo, {responseType: 'text'});
  }

  login(user: {name: string, password: string}): Observable<string> {
    return this.http.post(`${this.apiURI}/auth/authenticate`, user, { responseType: 'text' }).pipe(
      tap(token => {
        localStorage.setItem('authToken', token);
      })
    );
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('authToken') !== null;
  }
  

  setToken(token: string) {
    localStorage.setItem('authToken', token);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  setRoles(roles: string) {
    localStorage.setItem('roles', roles);
  }
  

  getRoles(): string {
    return localStorage.getItem('roles') || '';
  }
  

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token ? true : false;
  }

  hasRoles(roles: string[]): boolean {
    const userRoles = this.getRoles().split(',').map(role => role.trim());
    return roles.some(requiredRole => userRoles.includes(requiredRole));
  }
  
  

  logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');
  }
}
