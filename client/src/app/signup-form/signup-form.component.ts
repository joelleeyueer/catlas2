import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup-form',
  templateUrl: './signup-form.component.html',
  styleUrls: ['./signup-form.component.css']
})
export class SignupFormComponent implements OnInit {
  signupForm!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService,
    private snackBar: MatSnackBar, private router: Router) { }

  ngOnInit(): void {
    this.createForm();
  }

  createForm(): void {
    this.signupForm = this.fb.group({
      name: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
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
}
