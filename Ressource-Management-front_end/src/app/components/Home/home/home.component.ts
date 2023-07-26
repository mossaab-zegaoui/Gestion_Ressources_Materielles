import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  hasReloaded = false;
  constructor() {

  }
  ngOnInit(): void {
    const reloadAfterRedirect = sessionStorage.getItem('reloadAfterRedirect');
    if (reloadAfterRedirect === 'true') {
      sessionStorage.removeItem('reloadAfterRedirect');
      window.location.replace(window.location.href);
    }
  }



}
