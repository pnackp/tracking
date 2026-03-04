import { Component, signal } from '@angular/core';
import { ApiComponent } from '../../api.component';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-aside',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  standalone: true,
  templateUrl: './aside.html',
  styleUrl: './aside.css',
})

export class Aside {

  user = signal<any | null>(this.getUser());

  isOpen = signal(false);

  isSidebarOpen = false;
  constructor(private api: ApiComponent, private router: Router) { }

  private getUser() {
    const data = localStorage.getItem("userInfo");
    return data ? JSON.parse(data) : null;
  }

  isAdmin(){
    return this.user()?.role === 'admin';
  }

  isUser(){
    return this.user()?.role === 'user';
  }

  isDriver(){
    return this.user()?.role === 'driver';
  }

  isDriverMG(){
    return this.user()?.role === 'driver_manager';
  }

  logout() {
    this.api.LogoutRequest().subscribe({
      next: () => {
        localStorage.removeItem("userInfo");
        this.user.set(null);
        this.router.navigateByUrl('/login');
      },
      error: (err) => {
        console.error(err);
        localStorage.removeItem("userInfo");
        this.user.set(null);
        this.router.navigateByUrl('/login');
      }
    });
  }

}
