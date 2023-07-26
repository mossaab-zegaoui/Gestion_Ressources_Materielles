import { Component, OnInit } from '@angular/core';
import {
  MembreDepartement,
  Panne,
  PanneAction,
  Ressource,
  Technicien,
} from 'src/app/interface/Classes';
import { GestionPannesService } from '../../../services/gestion-pannes.service';
import { GestionDepartementsService } from '../../../services/gestion-departements.service';
import { HttpErrorResponse } from '@angular/common/http';

declare var $: any;

@Component({
  selector: 'app-constates-responsable.component.html',
  templateUrl: './constates-responsable.component.html',
  styleUrls: ['./constates-responsable.component.css'],
})
export class ConstatesResponsableComponent {
  public Pannes!: Panne[];
  public Ressources!: Ressource[];
  public Membresdepartements!: MembreDepartement[];
  public Techniciens!: Technicien[];
  public panneConstat: Panne | undefined;
  public panneActions: typeof PanneAction = PanneAction;

  constructor(
    private gestionPannesService: GestionPannesService,
    private gestionDepartementsService: GestionDepartementsService
  ) {}

  ngOnInit(): void {
    this.loadPannes();
    this.loadRessources();
    this.loadMembresDepartement();
    this.loadTechniciens();
  }

  public loadPannes() {
    this.Pannes = [];
    this.getPannesSevere();
  }

  public loadRessources() {
    this.Ressources = [];
    this.getAllRessources();
  }
  public loadMembresDepartement() {
    this.Membresdepartements = [];
    this.getAllMembresDepartement();
  }
  public loadTechniciens() {
    this.Techniciens = [];
    this.getAllTechniciens();
  }

  public getAllPannes(): void {
    this.gestionPannesService.getAllPannes().subscribe({
      next: (data: Panne[]) => {
        this.Pannes = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getPannesSevere(): void {
    this.gestionPannesService.getPanneWithConstatNotNull().subscribe({
      next: (data: Panne[]) => {
        this.Pannes = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getAllRessources(): void {
    this.gestionPannesService.getAllRessources().subscribe({
      next: (data: Ressource[]) => {
        this.Ressources = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getAllMembresDepartement() {
    this.gestionDepartementsService.getAllMembers().subscribe({
      next: (data) => {
        this.Membresdepartements = data;
      },
      error: (error: HttpErrorResponse) => console.log(error),
    });
  }

  public getAllTechniciens() {
    this.gestionPannesService.getAllTechniciens().subscribe({
      next: (data) => {
        this.Techniciens = data;
      },
      error: (error: HttpErrorResponse) => console.log(error),
    });
  }

  public getRessource(idRessource: number | null): Ressource | null {
    return this.Ressources.filter(
      (ressource) => ressource.id === idRessource
    )[0];
  }

  public getMemberDepartement(
    idMembreDepartement: String | null
  ): MembreDepartement | null {
    return this.Membresdepartements.filter(
      (membreDepartement) => membreDepartement.id === idMembreDepartement
    )[0];
  }

  public getTechnicien(idTechnicien: string | null): Technicien | null {
    return this.Techniciens.filter(
      (technicien) => technicien.id === idTechnicien
    )[0];
  }

  public handlePanneReparer(): void {
    if (this.panneConstat != undefined) {
      this.panneConstat.demande = this.panneActions.REPARER;
      this.gestionPannesService.editPanne(this.panneConstat).subscribe({
        next: (data) => {
          this.loadPannes();
          this.ngAfterViewInit();
        },
        error: (error) => {
          console.log(error);
        },
      });
      this.panneConstat = undefined;
    }
  }

  public handlePanneChanger(): void {
    if (this.panneConstat != undefined) {
      this.panneConstat.demande = this.panneActions.CHANGER;
      this.gestionPannesService.editPanne(this.panneConstat).subscribe({
        next: (data) => {
          this.loadPannes();
          this.ngAfterViewInit();
          $('#examinerConstatModal').modal('hide');
        },
        error: (error) => {
          console.log(error);
        },
      });
      this.panneConstat = undefined;
    }
  }

  public openModal(panne: Panne): void {
    this.panneConstat = panne;
  }

  public compareDate(date: Date): boolean {
    if (date == null) {
      return false;
    } else {
      if (date.toString() < this.formateDate(new Date())) return false;
      else return true;
    }
  }

  public formateDate(date: Date) {
    let today = date;
    let year = today.getFullYear();
    let month = String(today.getMonth() + 1).padStart(2, '0');
    let day = String(today.getDate()).padStart(2, '0');
    let formattedDate = `${year}-${month}-${day}`;
    return formattedDate;
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#pannesTable').DataTable();
      });
    }, 500);
  }
}
