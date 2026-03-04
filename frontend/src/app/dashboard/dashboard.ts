import { Component } from '@angular/core';
import { Aside } from './aside/aside';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [Aside , RouterOutlet],
  standalone : true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

}
