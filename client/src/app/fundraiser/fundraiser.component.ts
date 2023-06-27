import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatService } from '../cat.service';
import { Observable } from 'rxjs';
import { NavigationService } from '../navigation.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-fundraiser',
  templateUrl: './fundraiser.component.html',
  styleUrls: ['./fundraiser.component.css']
})
export class FundraiserComponent implements OnInit{

  fundraiser$: Observable<any> | undefined;
  catId: string;

  constructor(private route: ActivatedRoute, private catService: CatService, private navigationService: NavigationService,
    private router: Router, private snackBar: MatSnackBar) {
    this.catId = this.route.snapshot.paramMap.get('id')!;
  }

  
    ngOnInit(): void {
        const isAdmin = this.router.url.includes('admin'); 
        this.fundraiser$ = (isAdmin ? this.catService.getCatFundraiserAdmin(this.catId) : this.catService.getCatFundraiser(this.catId)).pipe(
          catchError(err => {
            // Display the snackbar with the error message
            this.snackBar.open(err, 'Close', {
              duration: 5000,
            });
            return throwError(err);
          })
        );
    }

  

  getTotalDonation(donations: {amount: number}[]) {
    return donations.reduce((total, donation) => total + donation.amount, 0);
  }

  getDonationPercentage(donations: {amount: number}[], goal: number) {
    const totalDonation = this.getTotalDonation(donations);
    return (totalDonation / goal) * 100;
  }

  // redirectTo(stripePaymentUrl: string) {
  //   this.router.navigateByUrl(stripePaymentUrl);
  // }

  goBack() {
    this.navigationService.goBack();
  }

  

}
