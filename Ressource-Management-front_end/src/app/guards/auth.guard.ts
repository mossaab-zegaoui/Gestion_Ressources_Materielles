import { Injectable } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';
import { EventTypes } from '../interface/event-type';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
    constructor(private auth: AuthService, private toastService: ToastService, private router: Router) { }
  canActivate(): boolean {
    if (this.auth.isLoggedIn()) {
      return true;
    }
    else {
      this.toastService.showWarningToast(EventTypes.Warning, "you need to login First");
      this.router.navigate(['login']);
      return false;
    }
  }

}
