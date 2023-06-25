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
      this.catId = params['catId']; // <-- Change this line
    });
  }

  submitForm(): void {
    if (this.form.valid) {
      const updateForm: UpdateForm = {
        type: this.form.get('type')?.value,
        catId: this.catId!,
        location: this.form.get('location')?.value,
        datetime: this.form.get('datetime')?.value,
        comments: this.form.get('comments')?.value,
        photo: this.form.get('photo')?.value,
        foodType: this.form.get('foodType')?.value,
        waterStatus: this.form.get('waterStatus')?.value
      };
  
      this.updateService.updateCat(updateForm).then(
        (response) => {
          console.log(response);
          this.snackBar.open('Update submitted successfully!', '', { duration: 2000 }); // <-- Show snackbar here
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
      waterStatus: ['']
    });
  }
  

}
