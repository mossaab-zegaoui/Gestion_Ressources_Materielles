import {Component} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastService} from "../../../services/toast.service";
import {EventTypes} from "../../../interface/event-type";

@Component({
  selector: 'app-confirm-registration',
  templateUrl: './confirm-registration.component.html',
  styleUrls: ['./confirm-registration.component.css']
})
export class ConfirmRegistrationComponent {

  constructor(private auth: AuthService, private activatedRoute: ActivatedRoute,
              private route: Router, private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.confirmRegistration();
  }

  confirmRegistration() {
    const token = this.activatedRoute.snapshot.queryParamMap.get('token');
    this.auth.confirmRegistration(token).subscribe({
      next: () => {
        this.route.navigate(['/login']);
        this.toastService.showSuccessToast(EventTypes.Success, "account verified\n you can login now");

      }
    })
  }
}
