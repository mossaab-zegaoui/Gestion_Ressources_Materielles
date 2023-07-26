import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, catchError, map, of, startWith } from 'rxjs';
import { resgisterState } from 'src/app/interface/appstate';
import { DataState } from 'src/app/interface/datastate';
import { EventTypes } from 'src/app/interface/event-type';
import { AuthService } from 'src/app/services/auth.service';
import { ToastService } from 'src/app/services/toast.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  isText: boolean = false;
  type: string = 'password';
  eyeIcon: string = 'bi-eye-slash';
  readonly DataState = DataState;
  registerState$: Observable<resgisterState> = of({
    dataState: DataState.LOADED,
    registerSuccess: false,
  });
  registerForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    email: ['', Validators.required],
    nomSociete: ['', Validators.required],
  });
  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private toastService: ToastService
  ) {}

  hideShowPassword() {
    this.isText = !this.isText;
    this.isText ? (this.eyeIcon = 'bi-eye') : (this.eyeIcon = 'bi-eye-slash');
    this.isText ? (this.type = 'text') : (this.type = 'password');
  }
  onRegister() {
    if (!this.registerForm.valid)
      return this.validateAllFormsFields(this.registerForm);

    this.registerState$ = this.auth
      .registerFournisseur(this.registerForm.value)
      .pipe(
        map((response) => {
            console.log(response);
          this.toastService.showSuccessToast(
            EventTypes.Success,
            'Register successfuly'
          );
          this.registerForm.reset();
          return { dataState: DataState.LOADED, registerSuccess: true };
        }),
        startWith({ dataState: DataState.LOADING, registerSuccess: false }),
        catchError((err) =>
          of({
            dataState: DataState.ERROR,
            error: err.message,
            registerSuccess: false,
          })
        )
      );
  }

  private validateAllFormsFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((field) => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormsFields(control);
      }
    });
  }
}
