import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CatListComponent } from './cat-list/cat-list.component';
import { CatMapComponent } from './cat-map/cat-map.component';
import { HomepageComponent } from './homepage/homepage.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CatInfoComponent } from './cat-info/cat-info.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule} from '@angular/material/snack-bar';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule} from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialogModule } from '@angular/material/dialog';
import { MatGridListModule } from '@angular/material/grid-list';
import { FundraiserComponent } from './fundraiser/fundraiser.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { UpdateFormComponent } from './update-form/update-form.component';
import { MatOptionModule } from '@angular/material/core';
import { MatRadioModule } from '@angular/material/radio';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AddCatFormComponent } from './add-cat-form/add-cat-form.component';
import { PendingRequestsComponent } from './pending-requests/pending-requests.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { SignUpComponent } from './sign-up/sign-up.component';
import { AddFundraiserFormComponent } from './add-fundraiser-form/add-fundraiser-form.component';
import { CircleMenuComponent } from './circle-menu/circle-menu.component';
import { MatMenuModule } from '@angular/material/menu';
import { LoginDialogComponent } from './login-dialog/login-dialog.component';
import { SignupFormComponent } from './signup-form/signup-form.component';

@NgModule({
  declarations: [
    AppComponent,
    CatListComponent,
    CatMapComponent,
    CatInfoComponent,
    HomepageComponent,
    DashboardComponent,
    FundraiserComponent,
    UpdateFormComponent,
    AddCatFormComponent,
    PendingRequestsComponent,
    SignUpComponent,
    AddFundraiserFormComponent,
    CircleMenuComponent,
    LoginDialogComponent,
    SignupFormComponent
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDialogModule,
    MatGridListModule,
    MatProgressBarModule,
    MatOptionModule,
    MatRadioModule,
    FlexLayoutModule,
    MatSelectModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatMenuModule

  ],
  entryComponents: [CatInfoComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
