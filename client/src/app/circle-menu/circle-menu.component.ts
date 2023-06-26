import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginDialogComponent } from '../login-dialog/login-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../auth-service.service';

@Component({
  selector: 'app-circle-menu',
  templateUrl: './circle-menu.component.html',
  styleUrls: ['./circle-menu.component.css']
})
export class CircleMenuComponent {
  username: string | null = localStorage.getItem('username');

  constructor(public dialog: MatDialog, private snackBar: MatSnackBar, private authService: AuthService) { }

  openLoginDialog(): void {
    this.dialog.open(LoginDialogComponent);
  }

  logout() {
    this.authService.logout();
    this.username = null;
    this.snackBar.open('Logged out successfully', 'Close', { duration: 2000 });


  }


  
  


}
