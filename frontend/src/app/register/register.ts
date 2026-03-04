import { Component, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, Validators, AbstractControl, ValidationErrors, FormControl } from '@angular/forms';
import { NgClass } from '@angular/common';
import { RegisterRequest } from '../app.dto';
import { ApiComponent } from '../api.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './register.html',
  styleUrl: './register.css',
})

export class Register {
  isClosing = false;
  showMsg = signal("");

  constructor(private api: ApiComponent , private router : Router) { }

  register = new FormGroup(
    {
      employee_id: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern(/^\d+$/)]
      }),
      first_name: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(4)]
      }),
      last_name: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email]
      }),
      password: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(6)]
      }),
      confirmPassword: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      phone_number: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern(/^[0-9+\-\s]+$/)]
      }),
    },
    { validators: this.passwordMatchValidator }
  );

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      return { passwordMismatch: true };
    }

    return null;
  }

  onSubmit() {
    if (this.register.invalid) {
      this.register.markAllAsTouched();
      this.showMsg.set("Please fill correctly.");
      this.isClosing = false;
      return;
    }

    const { employee_id, first_name, last_name, password, email, phone_number } =
      this.register.getRawValue();

    const payload: RegisterRequest = {
      employee_id,
      first_name,
      last_name,
      password,
      email,
      phone_number
    };

    this.api.RegisterRequest(payload).subscribe({
      next: (res) => {
        this.showMsg.set("Register successful");
        this.isClosing = false;
        this.register.reset();  
        this.register.markAsPristine();
        this.register.markAsUntouched();
        this.router.navigateByUrl('/login');
        console.log("Success:", res);
      },

      error: (err) => {
        console.error("Register error:", err);

        if (err.status === 409) {
          this.showMsg.set("Username or Email already exists.");
        } else if (err.status === 400) {
          this.showMsg.set("Invalid data sent.");
        } else {
          this.showMsg.set("Something went wrong.");
        }

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

  get f() {
    return this.register.controls;
  }

}

