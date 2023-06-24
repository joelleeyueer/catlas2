import { Component } from '@angular/core';
import { Cat } from '../model/model';
import { CatService } from '../cat.service';
import { ActivatedRoute } from '@angular/router';
import { CatList } from '../model/model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {

  cats!: Cat[];
  searchCoordinates: { lat: number; lng: number; } | null = null;

  constructor(private catService: CatService, private route: ActivatedRoute, private snackBar: MatSnackBar, private router: Router) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const address = params['address'];
      if (address) {
        // No longer making a call to catService.getCats() here.
        // Instead, expect CatListComponent to make the call and emit catsUpdated event.
      }
    });
  }


  onCatsUpdated(updatedCats: Cat[]): void {
    this.cats = updatedCats;
  
    const address = this.route.snapshot.queryParams['address'];
    if (address) {
      this.router.navigate(['/search'], { queryParams: { address: address } });
    }
  }

  onSearchCoordinatesUpdated(coordinates: { lat: number; lng: number }): void {
    this.searchCoordinates = coordinates;
  }

}
