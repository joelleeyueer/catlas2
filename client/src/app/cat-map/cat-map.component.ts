import { Component, OnInit, Input, SimpleChanges, OnChanges } from '@angular/core';
import { Loader } from '@googlemaps/js-api-loader';
import { Cat } from '../model/model';

@Component({
  selector: 'app-cat-map',
  templateUrl: './cat-map.component.html',
  styleUrls: ['./cat-map.component.css']
})
export class CatMapComponent implements OnInit {

  title = 'google-maps';
  private map: google.maps.Map | undefined;

  @Input()
  cats: Cat[] = [];


  ngOnInit(): void {
    const loader = new Loader({
      apiKey: 'AIzaSyDXy7SLzzZRQJyDba-z8DGqWXD-8xHQVhQ',
      version: "weekly",
    });

    loader.load().then(() => {
      console.log("Google Maps loaded");
      const location = {
        lat: 1.3537,
        lng: 103.7190
      }

      this.map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
        center: location,
        zoom: 16
      });

      this.updateMarkers()
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['cats'] && !changes['cats'].isFirstChange() && this.map) {
      this.updateMarkers();
    }
  }

  updateMarkers(): void {
    this.cats.forEach(cat => {
      cat.frequentLocations.forEach(loc => {
        new google.maps.Marker({
          position: { lat: loc.lat, lng: loc.long },
          map: this.map,
        });
      });
    });
  }
}