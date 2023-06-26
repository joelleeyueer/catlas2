import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatMapComponent } from './cat-map/cat-map.component';
import { HomepageComponent } from './homepage/homepage.component';
import { CatInfoComponent } from './cat-info/cat-info.component';
import { FundraiserComponent } from './fundraiser/fundraiser.component';
import { UpdateFormComponent } from './update-form/update-form.component';
import { AddCatFormComponent } from './add-cat-form/add-cat-form.component';
import { PendingRequestsComponent } from './pending-requests/pending-requests.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'search', component: HomepageComponent },
  { path: 'cat/:id', component: CatInfoComponent },
  { path: 'cat/:id/fundraiser', component: FundraiserComponent },
  { path: 'cat/:id/update', component: UpdateFormComponent },
  { path: 'addcat', component: AddCatFormComponent },
  { path: 'admin/requests', component: PendingRequestsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
