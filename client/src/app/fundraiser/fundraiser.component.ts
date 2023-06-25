import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatService } from '../cat.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-fundraiser',
  templateUrl: './fundraiser.component.html',
  styleUrls: ['./fundraiser.component.css']
})
export class FundraiserComponent implements OnInit{

  fundraiser$: Observable<any> | undefined;
  catId: string;

  constructor(private route: ActivatedRoute, private catService: CatService) {
    this.catId = this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.fundraiser$ = this.catService.getCatFundraiser(this.catId);
  }

  getTotalDonation(donations: {amount: number}[]) {
    return donations.reduce((total, donation) => total + donation.amount, 0);
  }

  getDonationPercentage(donations: {amount: number}[], goal: number) {
    const totalDonation = this.getTotalDonation(donations);
    return (totalDonation / goal) * 100;
  }
  

}
