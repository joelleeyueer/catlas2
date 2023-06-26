import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CatRequest, FundraiserRequest } from '../model/model';
import { RequestService } from '../request.service';
import { NavigationService } from '../navigation.service';


@Component({
  selector: 'app-pending-requests',
  templateUrl: './pending-requests.component.html',
  styleUrls: ['./pending-requests.component.css']
})
export class PendingRequestsComponent implements OnInit {

  catRequests: CatRequest[] = [];
  fundraiserRequests: FundraiserRequest[] = [];

  constructor(private requestService: RequestService, private navigationService: NavigationService) { }

  ngOnInit(): void {
    this.getCatRequests();
    this.getFundraiserRequests();
  }

  getCatRequests(): void {
    this.requestService.getCatRequests().subscribe(
      (data: CatRequest[]) => this.catRequests = data
    );
  }

  getFundraiserRequests(): void {
    this.requestService.getFundraiserRequests().subscribe(
      (data: FundraiserRequest[]) => this.fundraiserRequests = data
    );
  }

  approveCatRequest(catId: string): void {
    this.requestService.approveCatRequest(catId).subscribe(
      _ => this.getCatRequests() // Refresh cat requests after approval
    );
  }

  rejectCatRequest(catId: string): void {
    this.requestService.rejectCatRequest(catId).subscribe(
      _ => this.getCatRequests() // Refresh cat requests after rejection
    );
  }

  approveFundraiserRequest(fundId: string): void {
    this.requestService.approveFundraiserRequest(fundId).subscribe(
      _ => this.getFundraiserRequests() // Refresh fundraiser requests after approval
    );
  }

  rejectFundraiserRequest(fundId: string): void {
    this.requestService.rejectFundraiserRequest(fundId).subscribe(
      _ => this.getFundraiserRequests() // Refresh fundraiser requests after rejection
    );
  }

  goBack() {
    this.navigationService.goBack();
  }
}
