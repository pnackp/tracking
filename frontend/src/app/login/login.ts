import { Component, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { NgClass } from '@angular/common';
import { ApiComponent } from '../api.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {

  isClosing = false;
  showMsg = signal("");

  constructor(private Api: ApiComponent, private router: Router) { }

  form = new FormGroup({
    identify: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  Onclick() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.showMsg.set("Please fill in all fields.");
      this.isClosing = false;
      return;
    }

    this.Api.LoginRequest({
      identifier: this.form.value.identify!,
      password: this.form.value.password!
    }).subscribe({
      next: (res) => {
        this.showMsg.set("");

        localStorage.setItem("userInfo", JSON.stringify(res));

        this.router.navigateByUrl('/dashboard');
      },
      error: (err) => {

        let message = "Invalid or Waiting for Admin verify";
        if (err.status === 401 && err.error?.error) {
          message = err.error.error;
        }

        this.showMsg.set(message);
        this.isClosing = false;
      }
    });
  }

  CloseError() {
    this.isClosing = true;

    setTimeout(() => {
      this.showMsg.update(() => "");
      this.isClosing = false;
    }, 250);
  }
}