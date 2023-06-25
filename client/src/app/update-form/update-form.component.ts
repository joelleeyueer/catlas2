import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UpdateService } from '../update.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UpdateForm } from '../model/model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent implements OnInit{

  form!: FormGroup;
  updateForm: UpdateForm[] = [];
  private catId!: string | null;
  

  constructor(private fb: FormBuilder, private router: Router, 
            private route: ActivatedRoute, private updateService: UpdateService,
            private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.createForm();
    this.route.params.subscribe(params => {
      this.catId = params['id']; // <-- Change this line
      console.log('Received catId: ', this.catId); // <-- Log the catId here
    });
    
  }

  submitForm(): void {
    if (this.form.valid) {
      let foodTypes= ''; 
      if (this.form.get('treats')?.value) {
        foodTypes += 'treats'
        foodTypes += ' '
      }
      if (this.form.get('wetFood')?.value) {
        foodTypes += 'wet'
        foodTypes += ' '
      }
      if (this.form.get('dryFood')?.value) {
        foodTypes += 'dry'

      }

      const updateForm: UpdateForm = {
        type: this.form.get('type')?.value,
        catId: this.catId!,
        location: this.form.get('location')?.value,
        datetime: this.form.get('datetime')?.value,
        comments: this.form.get('comments')?.value,
        photo: this.form.get('photo')?.value,
        foodType: foodTypes,
        waterStatus: this.form.get('waterStatus')?.value
      };
  
      this.updateService.updateCat(updateForm).then(
        (response) => {
          console.log(response);
          if (response.success) {
            this.snackBar.open(response.success, '', { duration: 2000 }); // Show snackbar here
          } else if (response.error) {
            alert(response.error);
          }
          this.router.navigate(['/cat', updateForm.catId]);
        },
        (error) => {
          alert('There was an error in creating update. Please try again.');
          console.error(error);
        }
      )
    }
  }

  selectedFile: File | null = null;

  onFileSelected(event: any) {
      if (event.target.files.length > 0) {
          this.selectedFile = event.target.files[0];
          this.form.get('photo')?.setValue(this.selectedFile);
      }
  }


  private createForm(): void {
    this.form = this.fb.group({
      type: ['', Validators.required],
      location: ['', Validators.required],
      datetime: ['', Validators.required],
      comments: [''],
      photo: [''],
      foodType: this.fb.array([]),
      treats: [false],
      wetFood: [false],
      dryFood: [false],
      waterStatus: ['']
    });
  }
  

}
