import { Component, OnInit } from '@angular/core';
import { Panne, Ressource } from 'src/app/interface/Classes';
import { GestionPannesService } from '../../../services/gestion-pannes.service';
import { HttpErrorResponse } from '@angular/common/http';
declare var $: any;

@Component({
  selector: 'app-pannes-member-departement',
  templateUrl: './pannes-member-departement.component.html',
  styleUrls: ['./pannes-member-departement.component.css'],
})
export class PannesMemberDepartementComponent {
  public Pannes!: Panne[];
  public Ressources!: Ressource[];
  public userId!: string;
  public idDepartement!: number;
  constructor(private gestionPannesService: GestionPannesService) {}

  ngOnInit(): void {
    this.userId = localStorage.getItem('userId')!;
    this.idDepartement = Number(localStorage.getItem('departementId')!);
    this.loadPannes();
    this.loadRessources();
  }

  public loadPannes() {
    this.Pannes = [];
    this.getmemberDepartementPannes();
  }

  public loadRessources() {
    this.Ressources = [];
    this.getmemberDepartementRessources();
  }

  public getmemberDepartementPannes(): void {
    this.gestionPannesService
      .getMemberDepartementPannes(this.userId)
      .subscribe({
        next: (data: any) => {
          this.Pannes = data;
        },
        error: (error: HttpErrorResponse) => {
          console.log(error);
        },
      });
    // this.gestionPannesService.getMemberDepartementRessources;
  }

  public getmemberDepartementRessources(): void {
    this.gestionPannesService
      .getMemberDepartementRessources(this.userId)
      .subscribe({
        //to extract from the current user
        next: (data: Ressource[]) => {
          this.Ressources = data;
        },
        error: (error: HttpErrorResponse) => {
          console.log(error);
        },
      });
  }

  public getRessource(idRessource: number | null): Ressource | null {
    return this.Ressources.filter(
      (ressource) => ressource.id === idRessource
    )[0];
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#pannesTable').DataTable();
      });
    }, 500);
  }
}
