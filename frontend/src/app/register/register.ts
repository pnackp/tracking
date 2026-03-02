import { Component , signal } from '@angular/core';
import { ApiService, RegisterInterface } from '../app.api';
import { FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html'
})

export class RegisterComponent {
  constructor(private ServiceApi: ApiService, private Route: Router) { }

  form = new FormGroup({
    employee_id: new FormControl('', { nonNullable: true }),
    first_name: new FormControl('', { nonNullable: true }),
    last_name: new FormControl('', { nonNullable: true }),
    password: new FormControl('', { nonNullable: true }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    phone_number: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[0-9]{10}$/)
      ]
    })
  });

  isShow = signal("");  

  Onclick() {
    this.form.markAllAsTouched()
    if (this.form.invalid) {
      return;
    }

    console.log(this.form.value);
    const payload: RegisterInterface = this.form.value as RegisterInterface;
    this.ServiceApi.register(payload).subscribe({
      next: (res) => {
        console.log("Register success:", res);
        this.Route.navigateByUrl("/login");
      },
      error: (err) => {
        console.error("Register failed:", err);
        this.isShow.update(value => err.error.msg);
      },
      complete: () => {
        console.log("Request completed");
      }
    });
  }
}
