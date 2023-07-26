import { Component, OnInit } from '@angular/core';
import {
  Event,
  RouterEvent,
  Router,
  ActivatedRoute,
  ActivatedRouteSnapshot,
} from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from './services/auth.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'res_manager_front';
  currentRoute!: any;
  currentRouteData: any = {};

  constructor(public router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe((data) => {
      this.currentRouteData = data;
    });
  }
}
