import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService,
    private snackBar: MatSnackBar, private router: Router, public dialogRef: MatDialogRef<LoginDialogComponent>) {
    this.loginForm = this.fb.group({
      name: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe(() => {
        console.log('User logged in successfully');
        localStorage.setItem('username', this.loginForm.value.name);
        const username = this.loginForm.value.name;
        const role = username === 'admin' ? 'ROLE_ADMIN' : 'ROLE_USER';
        localStorage.setItem('roles', role);
        this.snackBar.open('Logged in successfully', 'Close', { duration: 2000 });
        this.dialogRef.close();  // close the dialog
      }, error => {
        console.error('There was an error during the login', error);
      });
    }
  }
  
}
