import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatMapComponent } from './cat-map/cat-map.component';
import { HomepageComponent } from './homepage/homepage.component';
import { CatInfoComponent } from './cat-info/cat-info.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'cat/:id', component: CatInfoComponent } // Add this route

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
