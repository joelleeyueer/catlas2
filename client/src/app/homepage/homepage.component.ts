import { Component } from '@angular/core';
import { Cat } from '../model/model';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {

  cats!: Cat[];
  searchCoordinates: { lat: number; lng: number; } | null = null;

  onCatsUpdated(cats: Cat[]): void {
    this.cats = cats;
  }

  onSearchCoordinatesUpdated(coordinates: { lat: number; lng: number }): void {
    this.searchCoordinates = coordinates;
  }

}
