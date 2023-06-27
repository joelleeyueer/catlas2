import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatMapComponent } from './cat-map/cat-map.component';
import { HomepageComponent } from './homepage/homepage.component';
import { CatInfoComponent } from './cat-info/cat-info.component';
import { FundraiserComponent } from './fundraiser/fundraiser.component';
import { UpdateFormComponent } from './update-form/update-form.component';
import { AddCatFormComponent } from './add-cat-form/add-cat-form.component';
import { PendingRequestsComponent } from './pending-requests/pending-requests.component';
import { AddFundraiserFormComponent } from './add-fundraiser-form/add-fundraiser-form.component';
import { AuthGuard } from './auth-guard.service';
import { SignUpComponent } from './sign-up/sign-up.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'search', component: HomepageComponent },
  { path: 'admin/cat/:id', component: CatInfoComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_ADMIN'] }  },
  { path: 'admin/cat/:id/fundraiser', component: FundraiserComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_ADMIN'] } },
  { path: 'cat/:id', component: CatInfoComponent },
  { path: 'cat/:id/fundraiser', component: FundraiserComponent },
  { path: 'cat/:id/update', component: UpdateFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER', 'ROLE_ADMIN'] }},
  { path: 'addcat', component: AddCatFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER', 'ROLE_ADMIN'] }},
  { path: 'admin/requests', component: PendingRequestsComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_ADMIN'] }},
  { path: 'cat/:id/createFundraiser', component: AddFundraiserFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER', 'ROLE_ADMIN'] } },
  { path: 'signup', component: SignUpComponent},
  { path: '**', redirectTo: '/', pathMatch: 'full' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
