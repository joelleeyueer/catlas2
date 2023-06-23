import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatService } from '../cat.service';
import { CatInfo } from '../model/model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-cat-info',
  templateUrl: './cat-info.component.html',
  styleUrls: ['./cat-info.component.css']
})
// export class CatInfoComponent {

//   cat: any;

//   constructor(private route: ActivatedRoute, private catService: CatService) {}

//   ngOnInit(): void {
//     const catId = this.route.snapshot.paramMap.get('id');
//     this.catService.getCatDetails(catId!).subscribe(
//       (response: any) => {
//         this.cat = response;
//       },
//       error => {
//         console.log(error);
//       }
//     );
//   }
// }

export class CatInfoComponent {
  cat: CatInfo | null = null;

  constructor(
    private dialogRef: MatDialogRef<CatInfoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { catId: string },
    private catService: CatService,
  ) {
    this.fetchCatInfo(data.catId);
  }

  fetchCatInfo(catId: string) {
    this.catService.getCatDetails(catId).subscribe(catInfo => {
      this.cat = catInfo;
    });
  }

  close() {
    this.dialogRef.close();
  }
}
