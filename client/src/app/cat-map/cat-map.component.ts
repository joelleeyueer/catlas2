import { Component, OnInit } from '@angular/core';
import { Loader } from '@googlemaps/js-api-loader';

@Component({
  selector: 'app-cat-map',
  templateUrl: './cat-map.component.html',
  styleUrls: ['./cat-map.component.css']
})
export class CatMapComponent implements OnInit {

  title = 'google-maps';
  private map: google.maps.Map | undefined;

  

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

      const catLocations = [
        { lat: 1.3538, lng: 103.7191 },
        { lat: 1.3539, lng: 103.7192 },
        { lat: 1.3540, lng: 103.7193 },
      ];

      catLocations.forEach((catLocation) => {
        new google.maps.Marker({
          position: catLocation,
          map: this.map,
        });
      });


      
    });
  }

  // getCurrentLocation(): void {
  //   if (navigator.geolocation) {
  //     navigator.geolocation.getCurrentPosition(
  //       (position) => {
  //         const latitude = position.coords.latitude;
  //         const longitude = position.coords.longitude;
  //         console.log("Latitude: " + latitude);
  //         console.log("Longitude: " + longitude);
  //         // Do something with the latitude and longitude values
  //       },
  //       (error) => {
  //         console.log("Error occurred while retrieving location: ", error);
  //       }
  //     );
  //   } else {
  //     console.log("Geolocation is not supported by this browser.");
  //   }
  // }

}
