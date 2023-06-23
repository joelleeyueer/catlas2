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
  private markers: google.maps.Marker[] = [];


  @Input()
  cats: Cat[] = [];

  @Input()
  searchCoordinates: { lat: number; lng: number } | null = null;

  ngOnInit(): void {
    const loader = new Loader({
      apiKey: 'AIzaSyDXy7SLzzZRQJyDba-z8DGqWXD-8xHQVhQ',
      version: "weekly",
    });
  
    loader.load().then(() => {
      console.log("Google Maps loaded");
      this.map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
        zoom: 16
      });
  
      // Try HTML5 geolocation
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position: GeolocationPosition) => {
            const pos = {
              lat: position.coords.latitude,
              lng: position.coords.longitude,
            };
  
            // Set the map's center to the user's location
            if (this.map) this.map.setCenter(pos);
            this.updateMarkers()
          },
          () => {
            // If geolocation fails, set the center to a default location
            this.handleLocationError(true, {lat: 1.3537, lng: 103.7190});
          }
        );
      } else {
        // Browser doesn't support Geolocation, set the center to a default location
        this.handleLocationError(false, {lat: 1.3537, lng: 103.7190});
      }
    });
  }

  handleLocationError(browserHasGeolocation: boolean, pos: google.maps.LatLngLiteral) {
    if (!this.map) return;

    this.map.setCenter(pos);

    // You can also show an info window with the error message
    const infoWindow = new google.maps.InfoWindow({
      content: browserHasGeolocation
        ? "Error: The Geolocation service failed."
        : "Error: Your browser doesn't support geolocation.",
      position: pos,
    });

    infoWindow.open(this.map);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['cats'] && !changes['cats'].isFirstChange()) {
      this.updateMarkers();
    }

    if (changes['searchCoordinates'] && this.map && changes['searchCoordinates'].currentValue) {
      this.map.setCenter(changes['searchCoordinates'].currentValue);
    }
  }
  

  updateMarkers(): void {

    this.markers.forEach(marker => marker.setMap(null));
    this.markers = [];

    this.cats.forEach(cat => {
      const marker = new google.maps.Marker({
        position: { lat: cat.coordinates.lat, lng: cat.coordinates.lng },
        map: this.map,
        icon: 'assets/images/catMarker.png'
      });
      this.markers.push(marker);
      
    });
  }

}