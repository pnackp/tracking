import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-verify',
  templateUrl: './verify-component.html'
})
export class VerifyComponent implements OnInit {

  status: 'loading' | 'success' | 'expired' | 'error' = 'loading';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit() {

    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.status = 'error';
      return;
    }

    this.http.get(`http://localhost:8080/api/auth/verify?token=${token}`)
      .subscribe({
        next: () => this.status = 'success',
        error: (err) => {

          if (err.error?.message?.includes('expired')) {
            this.status = 'expired';
          } else {
            this.status = 'error';
          }

        }
      });
  }
}