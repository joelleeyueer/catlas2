import { Component } from '@angular/core';
import { Cat } from '../model/model';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {

  cats!: Cat[];

  onCatsUpdated(cats: Cat[]): void {
    this.cats = cats;
  }

}
