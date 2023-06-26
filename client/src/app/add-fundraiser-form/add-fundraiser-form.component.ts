import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { UpdateService } from '../update.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AddFundraiserForm } from '../model/model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NavigationService } from '../navigation.service';
import { CatService } from '../cat.service';

@Component({
  selector: 'app-add-fundraiser-form',
  templateUrl: './add-fundraiser-form.component.html',
  styleUrls: ['./add-fundraiser-form.component.css']
})
export class AddFundraiserFormComponent implements OnInit{

  form!: FormGroup;
  addFundraiserForm: AddFundraiserForm[] = [];
  private catId!: string | null;
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder, private router: Router, 
    private route: ActivatedRoute, private catService: CatService,
    private snackBar: MatSnackBar, private navigationService: NavigationService) {}

  ngOnInit(): void {
    this.createForm();
    this.route.params.subscribe(params => {
      this.catId = params['id']; // <-- Change this line
      console.log('Received catId: ', this.catId); // <-- Log the catId here
    });
  }

  submitForm(): void {
    if (this.form.valid) {
      const addFundraiserForm: AddFundraiserForm = {
        photo: this.form.get('photo')?.value,
        catId: this.catId!,
        username: localStorage.getItem("username") || "anonymous",
        title: this.form.get('title')?.value,
        description: this.form.get('description')?.value,
        donationGoal: this.form.get('donationGoal')?.value,
        deadline: this.form.get('deadline')?.value
      };
  
      this.catService.fundraiserRequest(addFundraiserForm)
      .then((response) => {
        console.log(response);
        if (response.success) {
          this.snackBar.open(response.success, '', { duration: 2000 }); // Show snackbar here
        } else if (response.error) {
          alert(response.error);
        }
        this.router.navigate(['/']);
      })
      .catch((err) => {
        this.snackBar.open('Error occurred while adding fundraiser: ' + err, 'Close', {
          duration: 3000,
        });
      });
    } else {
      this.form.markAllAsTouched();
    }
  }
  


  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
        this.selectedFile = event.target.files[0];
        this.form.get('photo')?.setValue(this.selectedFile);
      }
  }


  private createForm(): void {
    this.form = this.fb.group({
      photo: [null, Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required],
      donationGoal: [0, Validators.required],
      deadline: [null, Validators.required]
    });
  }
  
  // //either location or locationAddress must be filled
  // locationValidator(group: FormGroup): ValidationErrors | null {
  //   const location = group.get('location')?.value;
  //   const locationAddress = group.get('locationAddress')?.value;
  
  //   if (!location && !locationAddress) {
  //     return { required: true };
  //   }
  
  //   return null;
  // }
  

  goBack() {
    this.navigationService.goBack();
  }

}