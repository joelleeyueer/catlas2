import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatService } from '../cat.service';
import { CatInfo } from '../model/model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NavigationService } from '../navigation.service';


@Component({
  selector: 'app-cat-info',
  templateUrl: './cat-info.component.html',
  styleUrls: ['./cat-info.component.css']
})
export class CatInfoComponent {

  cat: any;
  notFound: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router, private catService: CatService, private snackBar: MatSnackBar, private navigationService: NavigationService) {}

  ngOnInit(): void {
    const catId = this.route.snapshot.paramMap.get('id');
    this.catService.getCatDetails(catId!).subscribe(
      (response: any) => {
        this.notFound = false;
        this.cat = response;
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

  goToFundraiser(catId: string) {
    this.router.navigate(['/cat', catId, 'fundraiser']);
  }

  goBack() {
    this.navigationService.goBack();
  }
  
}

// export class CatInfoComponent {
//   cat: CatInfo | null = null;

//   constructor(
//     private dialogRef: MatDialogRef<CatInfoComponent>,
//     @Inject(MAT_DIALOG_DATA) public data: { catId: string },
//     private catService: CatService,
//   ) {
//     this.fetchCatInfo(data.catId);
//   }

//   fetchCatInfo(catId: string) {
//     this.catService.getCatDetails(catId).subscribe(catInfo => {
//       this.cat = catInfo;
//     });
//   }

//   close() {
//     this.dialogRef.close();
//   }
// }
