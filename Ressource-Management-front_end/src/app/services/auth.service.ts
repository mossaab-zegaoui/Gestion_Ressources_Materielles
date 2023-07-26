import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams,} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {map, Observable, tap} from 'rxjs';
import {Fournisseur} from '../interface/user.interface';
import {JwtHelperService} from '@auth0/angular-jwt';
import {UpdatePassword} from '../interface/updatePassword';
import {FormGroup} from '@angular/forms';
import {Key} from '../enum/key.enum';
import {Departement, Role} from "../interface/Classes";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/v1/users';
  private userPayload: any;

  constructor(private http: HttpClient) {
    this.userPayload = this.decodedToken();
  }

  registerFournisseur(registerForm: FormGroup): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(`${this.apiUrl}/register`, registerForm);
  }

  validateResetPassword(token: string | null): Observable<any> {
    let params = new HttpParams();
    if (token) {
      params = params.set('token', token);
    }
    return this.http.get('http://localhost:8080/api/v1/validateResetPassword', {
      params,
    });
  }

  updatePassword(updatePasswordForm: UpdatePassword): Observable<any> {
    return this.http.post(
      'http://localhost:8080/api/v1/changePassword',
      updatePasswordForm
    );
  }

  resetPassword$(email: string): Observable<any> {
    const params = new HttpParams().set('email', email);
    return this.http.post<string>('http://localhost:8080/api/v1/resetPassword', null, {
      params,
    });
  }

  confirmRegistration(token: string | null): Observable<any> {
    let params = new HttpParams();
    if (token) {
      params = params.set('token', token);
    }
    return this.http.get('http://localhost:8080/api/v1/registrationConfirm', {
      params,
    });
  }

  login$(username: string, password: string): Observable<Key> {
    return this.http
      .post(`${this.apiUrl}/authenticate`, {username, password})
      .pipe(tap(console.log));
  }

  refreshToken() {
    return this.http.post(`${this.apiUrl}/refreshToken`, null);
  }

  roles$(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.apiUrl}/roles`);
  }

  departements$(): Observable<Departement[]> {
    return this.http.get<Departement[]>(`http://localhost:8080/api/v1/departements`);
  }

  onLogout(token: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.apiUrl}/logout`, {headers: headers});
  }

  isLoggedIn() {
    return localStorage.getItem(Key.TOKEN);
  }

  decodedToken() {
    const jwtHelper = new JwtHelperService();
    const token = localStorage.getItem(Key.TOKEN)!;
    return jwtHelper.decodeToken(token);
  }


}
