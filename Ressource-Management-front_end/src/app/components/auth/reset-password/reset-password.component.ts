import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, catchError, map, of, startWith } from 'rxjs';
import { ResetState } from 'src/app/interface/appstate';
import { DataState } from 'src/app/interface/datastate';
import { EventTypes } from 'src/app/interface/event-type';
import { AuthService } from 'src/app/services/auth.service';
import { ToastService } from 'src/app/services/toast.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent {
  email!: string;
  resetPasswordState$: Observable<ResetState> = of({
    dataState: DataState.LOADED,
  });
  readonly DataState = DataState;
  resetForm: FormGroup = this.fb.group({
    email: ['', Validators.required],
  });
  constructor(
    private auth: AuthService,
    private toastService: ToastService,
    private fb: FormBuilder,
  ) {}
  resetPassword() {
    if (!this.resetForm.valid)
      return this.validateAllFormsFields(this.resetForm);
    this.resetPasswordState$ = this.auth
      .resetPassword$(this.resetForm.get('email')?.value)
      .pipe(
        map(() => {
          this.resetForm.reset();
          this.toastService.showSuccessToast(
            EventTypes.Success,
            'a reset link has been sent to your email'
          );
          return { dataState: DataState.LOADED, resetSuccess: true };
        }),
        startWith({ dataState: DataState.LOADING, resetSuccess: false }),
        catchError((err) =>
          of({
            dataState: DataState.ERROR,
            error: err,
            resetSuccess: false,
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
