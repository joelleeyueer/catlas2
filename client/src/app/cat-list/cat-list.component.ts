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


  constructor(private fb: FormBuilder, private router: Router, private catService: CatService) { }

  ngOnInit(): void {
    this.searchForm = this.createForm();
  }

  catSearch() {
    const address = this.searchForm.get('addressSearch')?.value;
    
  
    if (!isNaN(address)) {
      this.catService.getCats(address).subscribe(
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
      console.error('Invalid address values');
    }
  }
  

  private createForm() {
    return new FormBuilder().group({
      addressSearch: ['', Validators.required]
    });
  }
}
