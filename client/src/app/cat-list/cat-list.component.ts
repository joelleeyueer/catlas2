import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CatService } from '../cat.service';
import { CatList, Cat, CatInfo } from '../model/model';
import { Output, EventEmitter } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { CatInfoComponent } from '../cat-info/cat-info.component';


@Component({
  selector: 'app-cat-list',
  templateUrl: './cat-list.component.html',
  styleUrls: ['./cat-list.component.css']
})
export class CatListComponent {

  error: string = '';
  searchForm!: FormGroup;
  cats: Cat[] = [];

  @Output() 
  catsUpdated: EventEmitter<Cat[]> = new EventEmitter<Cat[]>();

  @Output()
  searchCoordinatesUpdated: EventEmitter<{ lat: number; lng: number }> = new EventEmitter();

  constructor(private fb: FormBuilder, 
              private router: Router, 
              private route: ActivatedRoute,
              private catService: CatService, 
              private snackBar: MatSnackBar, 
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.searchForm = this.createForm();

    this.route.queryParams.subscribe(params => {
      const address = params['address'];
      if (address) {
        this.catSearch(address);
      }
    });
  }

  catSearch(address: string = '') {
    address = address || this.searchForm.get('addressSearch')?.value || '';
    this.cats = [];
    this.catService.getCats(address).subscribe(
      (response: CatList) => {
        this.cats = response.catList;
        this.catsUpdated.emit(this.cats);
        this.searchCoordinatesUpdated.emit(response.searchCoordinates);

        setTimeout(() => {
          this.router.navigate(['/search'], { queryParams: { address: address } });
        }, 0);
      },
      error => {
        const errorMessage = error.error?.error || 'An error occurred';
        this.snackBar.open(errorMessage, 'Close', {
          duration: 6000,
        });
      }
    );
  }



  navigateToDetail(catId: string) {
    this.router.navigate(['/cat', catId]);
  }

  openCatInfo(catId: string) {
    const dialogRef = this.dialog.open(CatInfoComponent, {
      data: { catId: catId },
      width: '80%',
      height: '80%'
    });
  }

  private createForm() {
    return new FormBuilder().group({
      addressSearch: ['', Validators.required]
    });
  }
}
