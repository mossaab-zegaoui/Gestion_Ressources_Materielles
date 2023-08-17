import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators,} from '@angular/forms';
import {Router} from '@angular/router';
import {catchError, map, Observable, of, startWith} from 'rxjs';
import {LoginState} from 'src/app/interface/appstate';
import {DataState} from 'src/app/interface/datastate';
import {EventTypes} from 'src/app/interface/event-type';
import {Key} from 'src/app/enum/key.enum';
import {AuthService} from 'src/app/services/auth.service';
import {ToastService} from 'src/app/services/toast.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });
  departementId!: any;
  userId!: any;
  loginState$: Observable<LoginState> = of({dataState: DataState.LOADED});
  readonly DataState = DataState;

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private toastService: ToastService,
  ) {
  }

  onLogin() {
    if (!this.loginForm.valid)
      return this.validateAllFormsFields(this.loginForm);
    this.loginState$ = this.authService
      .login$(this.loginForm.value.username, this.loginForm.value.password)
      .pipe(
        map((response: any) => {
          localStorage.removeItem(Key.TOKEN);
          localStorage.removeItem(Key.REFRESH_TOKEN);
          localStorage.removeItem("departementId");
          localStorage.setItem(Key.TOKEN, response.accessToken);
          localStorage.setItem(Key.REFRESH_TOKEN, response.refreshToken);
          localStorage.setItem("departementId",this.authService.decodedToken()!.departementId);
          this.router.navigate(['/home']);
          this.toastService.showSuccessToast(
            EventTypes.Success,
            'login successfuly'
          );
          return {dataState: DataState.LOADED, loginSuccess: true};
        }),
        startWith({dataState: DataState.LOADING, loginSuccess: false}),
        catchError((err) => {
          console.log(err);
          return of({dataState: DataState.LOADED, loginSuccess: false});
        })
      );
  }

  private validateAllFormsFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((field) => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({onlySelf: true});
      } else if (control instanceof FormGroup) {
        this.validateAllFormsFields(control);
      }
    });
  }
}
