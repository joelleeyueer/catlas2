import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  user = {
    name: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) { }

  login() {
    this.authService.login(this.user).subscribe((token: string) => {
        this.authService.setToken(token);
        alert('Login successful!');
        this.router.navigate(['home']);
      }, error => {
        alert('Login failed!');
        console.error(error);
      });
  }
}
