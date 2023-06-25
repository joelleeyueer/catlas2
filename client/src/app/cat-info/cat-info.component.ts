import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatService } from '../cat.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NavigationService } from '../navigation.service';
import { Loader } from '@googlemaps/js-api-loader';


@Component({
  selector: 'app-cat-info',
  templateUrl: './cat-info.component.html',
  styleUrls: ['./cat-info.component.css']
})
export class CatInfoComponent {

  cat: any;
  notFound: boolean = false;
  private map: google.maps.Map | undefined;
  private markers: google.maps.Marker[] = [];


  constructor(private route: ActivatedRoute, private router: Router, private catService: CatService, private snackBar: MatSnackBar, private navigationService: NavigationService) {}

  ngOnInit(): void {
    const loader = new Loader({
      apiKey: 'AIzaSyDXy7SLzzZRQJyDba-z8DGqWXD-8xHQVhQ',
      version: "weekly",
    });
    

    const catId = this.route.snapshot.paramMap.get('id');
    this.catService.getCatDetails(catId!).subscribe(
      (response: any) => {
        this.notFound = false;
        this.cat = response;

        loader.load().then(() => {
          this.map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
            center: {lat: this.cat?.frequentLocations[0]?.lat, lng: this.cat?.frequentLocations[0]?.lng},
            zoom: 18.5,
            mapId: 'bc32e652a4b92901'
          });
          this.updateMarkers();
        });
      },
      error => {
        console.log(error);
        const errorMessage = error.error?.error || 'An error occurred';
        // If the error message is "Cat not found", set notFound to true
        if (errorMessage === 'Cat not found') {
          this.notFound = true;
        }
        this.snackBar.open(errorMessage, 'Close', {
          duration: 6000,
        });
      }
    );
  }

  updateMarkers(): void {
    if(!this.map || !this.cat) return;
  
    this.markers.forEach(marker => marker.setMap(null));
    this.markers = [];
  
    this.cat.frequentLocations.forEach((location: { lat: any; lng: any; }) => {
      const marker = new google.maps.Marker({
        position: { lat: location.lat, lng: location.lng },
        map: this.map,
        icon: 'assets/images/catMarker.png'
      });
      this.markers.push(marker);
    });
  }
  

  goToFundraiser(catId: string) {
    this.router.navigate(['/cat', catId, 'fundraiser']);
  }

  goToUpdateForm(catId: string) {
    this.router.navigate(['/cat', catId, 'update']);
  }
  

  goBack() {
    this.navigationService.goBack();
  }
  
}
