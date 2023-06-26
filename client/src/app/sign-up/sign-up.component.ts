import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth-service.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent {
  userInfo = {
    name: '',
    password: '',
    email: '',
    roles: ''
  };

  constructor(private authService: AuthService, private router: Router) { }

  signUp() {
    this.authService.signUp(this.userInfo).subscribe(() => {
        alert('User registered successfully!');
        this.router.navigate(['login']);
      }, error => {
        alert('User registration failed!');
        console.error(error);
      });
  }
}
