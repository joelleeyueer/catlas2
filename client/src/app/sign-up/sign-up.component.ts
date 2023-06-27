import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth-service.service';
import { NavigationService } from '../navigation.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent {
  signupForm!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private navigationService: NavigationService,
    private snackBar: MatSnackBar, private router: Router) { }

  ngOnInit() {
    this.signupForm = this.createForm();
  }

  onSubmit() {
    if (this.signupForm.valid) {
      this.authService.signUp(this.signupForm.value).subscribe(() => {
        console.log('User registered successfully');
        this.snackBar.open('Registered successfully!', 'Close', { duration: 2000 });
        this.router.navigateByUrl('/')
      }, error => {
        console.error('There was an error during the registration', error);
      });
    }
  }

  private createForm() {
    return this.fb.group({
      name: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required]
    });
  }

  goBack() {
    this.navigationService.goBack();
  }

}
