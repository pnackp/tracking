import { Component } from '@angular/core';
import { ApiService, LoginInterface } from '../app.api';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { signal } from '@angular/core';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './login.html'
})

export class LoginComponent {

  constructor(private ServiceApi: ApiService, private Route: Router) { }

  isShow = signal("");  

  form = new FormGroup({
    identifier: new FormControl('', { nonNullable: true }),
    password: new FormControl('', { nonNullable: true })

  });

  Onclick() {
    this.form.markAllAsTouched()

    if (this.form.invalid) {
      return;
    }
    const payload: LoginInterface = this.form.value as LoginInterface;
    this.ServiceApi.login(payload).subscribe({
      next: (res) => {
        console.log("Login success:", res);
        this.Route.navigateByUrl("/dashboard");
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 401) {
          console.log("Unauthorized");
          this.isShow.update(value => "Incorrect username or password. Please wait for admin verification");
        } else if (err.status === 404) {
          console.log("Not found");
          this.isShow.update(value => err.error.msg);
        } else if (err.status === 500) {
          console.log("Server error");
        }
      },
      complete: () => {
        console.log("Request completed");
      }
    });
  }

}
