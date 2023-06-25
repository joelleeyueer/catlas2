import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { UpdateService } from '../update.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AddCatForm } from '../model/model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NavigationService } from '../navigation.service';
import { CatService } from '../cat.service';


@Component({
  selector: 'app-add-cat-form',
  templateUrl: './add-cat-form.component.html',
  styleUrls: ['./add-cat-form.component.css']
})
export class AddCatFormComponent implements OnInit{

  form!: FormGroup;
  addCatForm: AddCatForm[] = [];
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder, private router: Router, 
    private route: ActivatedRoute, private catService: CatService,
    private snackBar: MatSnackBar, private navigationService: NavigationService) {}

  ngOnInit(): void {
    this.createForm();
  }

  submitForm(): void {
    if (this.form.valid) {
      const addCatForm: AddCatForm = {
        profilePhoto: this.form.get('profilePhoto')?.value,
        location: this.form.get('location')?.value,
        locationAddress: this.form.get('locationAddress')?.value,
        name: this.form.get('name')?.value,
        gender: this.form.get('gender')?.value,
        birthday: this.form.get('birthday')?.value,
        sterilization: this.form.get('sterilization')?.value,
        personalityTraits: this.form.get('personalityTraits')?.value,
        dietLikes: this.form.get('dietLikes')?.value,
        dietDislikes: this.form.get('dietDislikes')?.value,
        feedingNotes: this.form.get('feedingNotes')?.value,
      };
  
      this.catService.addCatRequest(addCatForm)
        .then(response => {
          this.snackBar.open('Cat added successfully', 'Close', {
            duration: 2000,
          });
  
          this.goBack();
        })
        .catch(err => {
          this.snackBar.open('Error occurred while adding cat: ' + err, 'Close', {
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
      profilePhoto: ['', Validators.required],
      location: ['', Validators.required],
      locationAddress: [''],
      name: ['', Validators.required],
      gender: ['', Validators.required],
      birthday: ['', Validators.required],
      sterilization: [false],
      personalityTraits: [[]],
      dietLikes: [[]],
      dietDislikes: [[]],
      feedingNotes: [[]],
    });
  }
  
  //either location or locationAddress must be filled
  locationValidator(group: FormGroup): ValidationErrors | null {
    const location = group.get('location')?.value;
    const locationAddress = group.get('locationAddress')?.value;
  
    if (!location && !locationAddress) {
      return { required: true };
    }
  
    return null;
  }
  

  goBack() {
    this.navigationService.goBack();
  }

}
