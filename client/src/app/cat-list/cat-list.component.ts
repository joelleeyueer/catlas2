import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CatService } from '../cat.service';
import { CatList, Cat } from '../model/model';
import { Output, EventEmitter } from '@angular/core';


@Component({
  selector: 'app-cat-list',
  templateUrl: './cat-list.component.html',
  styleUrls: ['./cat-list.component.css']
})
export class CatListComponent {

  searchForm!: FormGroup;
  cats: Cat[] = [];

  @Output() 
  catsUpdated: EventEmitter<Cat[]> = new EventEmitter<Cat[]>();


  // cats = [
  //   {imageUrl: '/assets/images/nala.jpg', name: 'Cat 1', latestUpdate: 'Fed by @walnads - 6 hours ago'},
  //   {imageUrl: '/assets/images/nala.jpg', name: 'Cat 2', latestUpdate: 'Fed by @illeeterate - 2 hours ago'},
  //   {imageUrl: '/assets/images/nala.jpg', name: 'Cat 3', latestUpdate: 'Fed by @anotheruser - 1 day ago'},
  //   {imageUrl: '/assets/images/nala.jpg', name: 'Cat 4', latestUpdate: 'Fed by @walnads - 6 hours ago'},
  //   {imageUrl: '/assets/images/nala.jpg', name: 'Cat 5', latestUpdate: 'Fed by @username - 2 hours ago'},
  //   {imageUrl: '/assets/images/nala.jpg', name: 'Cat 6', latestUpdate: 'Fed by @anotheruser - 1 day ago'},
  // ];

  constructor(private fb: FormBuilder, private router: Router, private catService: CatService) { }

  ngOnInit(): void {
    this.searchForm = this.createForm();
  }

  catSearch() {
    const address = this.searchForm.get('addressSearch')?.value;
    const [lat, lng] = address.split(',').map((coord: string) => parseFloat(coord.trim()));
    console.log('printing lat and lng' + lat, lng);
  
    if (!isNaN(lat) && !isNaN(lng)) {
      this.catService.getCats(lat, lng).subscribe(
        (response: CatList) => {
          // Map the response to the format of the cats array
          this.cats = response.cats.map((cat) => {
            return {
              name: cat.name,
              feedingnotes: cat.feedingnotes,
              frequentLocations: cat.frequentLocations,
              // Use a default image URL for now
              imageUrl: '/assets/images/nala.jpg',
            }
          });
          this.catsUpdated.emit(this.cats);

        },
        error => {
          console.error('Error occurred while searching for cats:', error);
        }
      );
      
    } else {
      console.error('Invalid latitude or longitude values');
    }
  }
  

  private createForm() {
    return new FormBuilder().group({
      addressSearch: ['', Validators.required]
    });
  }
}
