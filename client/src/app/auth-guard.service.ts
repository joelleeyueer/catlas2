import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from './auth-service.service';
import { Observable, Subject, timer } from 'rxjs';
import { LoginDialogComponent } from './login-dialog/login-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { takeUntil } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  private activityTimeout = 5 * 1000 * 60; // 5 minutes in milliseconds
  private activityTimer$ = new Subject<void>();
  
  constructor(private authService: AuthService, private router: Router,
    private dialog: MatDialog) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let isLoggedIn = this.authService.isLoggedIn();
    
    if (!isLoggedIn) {
      this.dialog.open(LoginDialogComponent);
    }

    const requiredRoles = next.data['roles'] as string[];

    if (requiredRoles && requiredRoles.length > 0 && !this.authService.hasRoles(requiredRoles)) {
      this.router.navigate(['/']);
      return false;
    }

    if (isLoggedIn) {
      // Start the activity timer
      this.resetActivityTimer();
    }

    return isLoggedIn;
  }

  private resetActivityTimer(): void {
    // Reset the activity timer
    this.activityTimer$.next();

    // Subscribe to the timer observable and log out the user after the specified timeout
    timer(this.activityTimeout)
      .pipe(takeUntil(this.activityTimer$))
      .subscribe(() => {
        this.authService.logout();
        this.router.navigate(['/login']);
      });
  }
}
