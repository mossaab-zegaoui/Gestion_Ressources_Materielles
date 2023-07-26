import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../services/toast.service';
import { UpdatePassword } from 'src/app/interface/updatePassword';
import { EventTypes } from 'src/app/interface/event-type';
import {
  Observable,
  catchError,
  of,
  startWith,
  switchMap,
  tap,
} from 'rxjs';
import { DataState } from 'src/app/interface/datastate';
import { updatePasswordState } from 'src/app/interface/appstate';
@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css'],
})
export class UpdatePasswordComponent {
  token!: string | null;
  readonly DataState = DataState;
  updatePasswordState$: Observable<updatePasswordState> = of({
    dataState: DataState.LOADED,
    updateSuccess: false,
  });

  updateForm = this.fb.group(
    {
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    },
    { validators: this.passwordMatchValidator }
  );
  constructor(
    private auth: AuthService,
    private activatedRoute: ActivatedRoute,
    private route: Router,
    private toastService: ToastService,
    private fb: FormBuilder
  ) {}

  updatePassword(): void {
    const token = this.activatedRoute.snapshot.queryParamMap.get('token');
    if (!token) return;
    if (!this.updateForm.valid) {
      this.validateAllFormsFields(this.updateForm);
    }
    const updatePasswordForm: UpdatePassword = {
      token: token,
      newPassword: this.updateForm.get('newPassword')?.value,
      confirmPassword: this.updateForm.get('confirmPassword')?.value,
    };

    this.updatePasswordState$ = this.auth
      .validateResetPassword(updatePasswordForm.token)
      .pipe(
        switchMap(() => this.auth.updatePassword(updatePasswordForm)),
        tap((response) => {
          this.toastService.showSuccessToast(
            EventTypes.Success,
            'Your password has been updated'
          );
          this.updateForm.reset();
          this.route.navigate(['/login']);
          return {
            dataState: DataState.LOADED,
            message: response,
            updateSuccess: true,
          };
        }),
        startWith({ dataState: DataState.LOADING, updateSuccess: false }),
        catchError((err) => {
          console.error(err.message);
          return of({
            dataState: DataState.ERROR,
            updateSuccess: false,
            error: err.message,
          });
        })
      );
  }
  private passwordMatchValidator(formGroup: FormGroup) {
    const newPassword = formGroup.get('newPassword')!.value;
    const confirmPassword = formGroup.get('confirmPassword')!.value;
    return newPassword === confirmPassword ? null : { passwordMismatch: true };
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
