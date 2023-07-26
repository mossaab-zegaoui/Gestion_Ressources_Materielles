import { Component, OnInit } from '@angular/core';
import {
  Panne,
  Ressource,
  OrdrePanne,
  PanneFrequence,
  RessourceEnPanne,
} from 'src/app/interface/Classes';
import { GestionPannesService } from '../../../services/gestion-pannes.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
declare var $: any;
@Component({
  selector: 'app-pannes-technicien',
  templateUrl: './pannes-technicien.component.html',
  styleUrls: ['./pannes-technicien.component.css'],
})
export class PannesTechnicienComponent implements OnInit {
  public Pannes!: Panne[];
  public Ressources!: Ressource[];
  public editPanne: Panne | undefined;
  public ordres: typeof OrdrePanne = OrdrePanne;
  public frequences: typeof PanneFrequence = PanneFrequence;
  public severe = false;
  public explicationInvalid = false;
  public ordreInvalid = false;
  public frequenceInvalid = false;
  public constatInvalid = false;
  userId!: string;
  ressourcesEnPanne!: RessourceEnPanne[];
  constructor(private gestionPannesService: GestionPannesService) {}

  ngOnInit(): void {
    this.userId = localStorage.getItem('userId')!;

    this.loadPannes();
    this.loadRessources();
    this.severe = false;
  }

  public loadPannes() {
    this.Pannes = [];
    this.getPannesNotTreated();
  }

  public loadAllPannes() {
    this.Pannes = [];
    this.getAllPannes();
  }

  public loadRessources() {
    this.Ressources = [];
    this.getAllRessources();
  }

  public Severe() {
    this.severe = true;
  }
  public notSevere() {
    this.severe = false;
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

  // public getAllPanes(): void {
  //   this.gestionPannesService.getPannesAvecRessources().subscribe({
  //     next: (response: RessourceEnPanne[]) => {
  //       this.ressourcesEnPanne = response;
  //       console.warn('pannes avec ressources', this.ressourcesEnPanne);
  //     },
  //     error: (err) => console.log(err),
  //   });
  // }

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

  public getPannesNotTreated(): void {
    this.gestionPannesService.getPannesNotTreated().subscribe({
      next: (data: Panne[]) => {
        this.Pannes = data;
        console.warn(this.Pannes);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getRessource(idRessource: number | null): Ressource | null {
    return this.Ressources.find((ressource) => ressource.id === idRessource)!;
  }

  public handlePanneTraiter(): void {
    if (this.editPanne != undefined) {
      let panne: Panne = {
        id: this.editPanne!.id,
        idMembreDepartement: this.editPanne!.idMembreDepartement,
        explication: null,
        idRessource: this.editPanne!.idRessource,
        dateApparition: this.editPanne!.dateApparition,
        isTreated: true,
        constat: null,
        dateConstat: null,
        ordre: null,
        frequence: null,
        idTechnicien: this.userId,
        demande: null,
      };
      this.gestionPannesService.editPanne(panne).subscribe({
        next: (data) => {
          this.getPannesNotTreated();
          this.getAllRessources();
          this.ngAfterViewInit();
        },
        error: (error) => {
          console.log(error);
        },
      });
      this.editPanne = undefined;
    }
  }

  public handleEditPanne(editPanneForm: NgForm): void {
    console.log(editPanneForm);
    if (editPanneForm.valid) {
      let panne: Panne = {
        id: this.editPanne!.id,
        idMembreDepartement: this.editPanne!.idMembreDepartement,
        explication: editPanneForm.value.explication,
        idRessource: this.editPanne!.idRessource,
        dateApparition: this.editPanne!.dateApparition,
        isTreated: true,
        constat: editPanneForm.value.constat,
        dateConstat: new Date(),
        ordre: editPanneForm.value.ordre,
        frequence: editPanneForm.value.frequence,
        idTechnicien: this.userId,
        demande: null,
      };
      this.gestionPannesService.editPanne(panne).subscribe({
        next: (data) => {
          this.getPannesNotTreated();
          this.getAllRessources();
          this.ngAfterViewInit();
        },
        error: (error) => {
          console.log(error);
        },
      });
      this.editPanne = undefined;
      $('#traiterPanneModal').modal('hide');
    } else {
      if (editPanneForm.value.explication == '') this.explicationInvalid = true;
      else this.explicationInvalid = false;
      if (editPanneForm.value.ordre == '') this.ordreInvalid = true;
      else this.ordreInvalid = false;
      if (editPanneForm.value.frequence == '') this.frequenceInvalid = true;
      else this.frequenceInvalid = false;
      if (editPanneForm.value.constat == '') this.constatInvalid = true;
      else this.constatInvalid = false;
    }
  }

  public openModal(panne: Panne, mode: string): void {
    if (mode == 'traiterPanne') this.editPanne = panne;
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#pannesTable').DataTable();
      });
    }, 500);
    this.severe = false;
  }
}
