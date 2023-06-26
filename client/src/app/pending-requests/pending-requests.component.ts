import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CatRequest, CatRequests, FundraiserRequest, FundraiserRequests } from '../model/model';
import { RequestService } from '../request.service';
import { NavigationService } from '../navigation.service';
import { delay } from 'rxjs/operators';

@Component({
  selector: 'app-pending-requests',
  templateUrl: './pending-requests.component.html',
  styleUrls: ['./pending-requests.component.css']
})
export class PendingRequestsComponent implements OnInit {

  catRequests: CatRequests = { pendingCats: []}
  fundraiserRequests: FundraiserRequests = { pendingFunds: []}

  constructor(
    private requestService: RequestService,
    private navigationService: NavigationService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.getCatRequests();
    this.getFundraiserRequests();
  }

  getCatRequests(): void {
    this.requestService.getCatRequests().subscribe(
      (data: CatRequests) => this.catRequests = data
    );
  }

  getFundraiserRequests(): void {
    this.requestService.getFundraiserRequests().subscribe(
      (data: FundraiserRequests) => this.fundraiserRequests = data
    );
  }

  approveCatRequest(catId: string): void {
    this.requestService.approveCatRequest(catId).pipe(
      delay(1000) // Delay by 1 second
    ).subscribe(
      _ => {
        this.getCatRequests(); // Refresh cat requests after approval
        this.snackBar.open('Request approved successfully', 'Close', { duration: 3000 });
      }
    );
  }

  rejectCatRequest(catId: string): void {
    this.requestService.rejectCatRequest(catId).pipe(
      delay(1000) // Delay by 1 second
    ).subscribe(
      _ => {
        this.getCatRequests(); // Refresh cat requests after rejection
        this.snackBar.open('Request rejected successfully', 'Close', { duration: 3000 });
      }
    );
  }

  approveFundraiserRequest(fundId: string): void {
    this.requestService.approveFundraiserRequest(fundId).pipe(
      delay(1000) // Delay by 1 second
    ).subscribe(
      _ => {
        this.getFundraiserRequests(); // Refresh fundraiser requests after approval
        this.snackBar.open('Request approved successfully', 'Close', { duration: 3000 });
      }
    );
  }

  rejectFundraiserRequest(fundId: string): void {
    this.requestService.rejectFundraiserRequest(fundId).pipe(
      delay(1000) // Delay by 1 second
    ).subscribe(
      _ => {
        this.getFundraiserRequests(); // Refresh fundraiser requests after rejection
        this.snackBar.open('Request rejected successfully', 'Close', { duration: 3000 });
      }
    );
  }

  goBack() {
    this.navigationService.goBack();
  }
}
