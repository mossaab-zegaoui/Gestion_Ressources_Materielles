import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot} from '@angular/router';
import {ToastService} from '../services/toast.service';
import {EventTypes} from '../interface/event-type';
import {AuthService} from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class HasRoleGuard {
  constructor(private toastService: ToastService, private auth: AuthService) {
  }
  canActivate(route: ActivatedRouteSnapshot): boolean {
    let hasRole!: boolean;
    const roles: string[] = this.auth.decodedToken().ROLES.map(
      (role: { authority: string }) => role.authority
    );
    if (roles.some(item => route.data['role'].includes(item))) {
      hasRole = true;
    } else {
      this.toastService.showWarningToast(EventTypes.Warning, "Unauthorized access");
      hasRole = false;
    }
    return hasRole;
  }


}
