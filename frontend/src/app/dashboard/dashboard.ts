import { Component , OnInit } from '@angular/core';
import { Aside } from './aside/aside';
import { Router, RouterOutlet } from '@angular/router';
import { ApiComponent } from '../api.component';

@Component({
  selector: 'app-dashboard',
  imports: [Aside , RouterOutlet],
  standalone : true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})

export class Dashboard implements OnInit {

  constructor(private api: ApiComponent, private router: Router) {}

  ngOnInit() {
    this.api.CheckPremission().subscribe({
      next: () => {
        console.log("pass");
      },
      error: () => {
        this.router.navigateByUrl("/login");
      }
    });
  }
}