import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatMapComponent } from './cat-map/cat-map.component';
import { HomepageComponent } from './homepage/homepage.component';
import { CatInfoComponent } from './cat-info/cat-info.component';
import { FundraiserComponent } from './fundraiser/fundraiser.component';
import { UpdateFormComponent } from './update-form/update-form.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'search', component: HomepageComponent },
  { path: 'cat/:id', component: CatInfoComponent },
  { path: 'cat/:id/fundraiser', component: FundraiserComponent },
  { path: 'cat/:id/update', component: UpdateFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
