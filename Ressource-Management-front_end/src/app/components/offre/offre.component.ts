import { Component } from '@angular/core';
import { GestionAppelOffreService } from '../../services/gestion-appel-offre.service';
import { OffreService } from '../../services/offre.service';
import {
  AppelOffre,
  Imprimante,
  Offre,
  Ordinateur,
  Ressource,
  RessourceFournisseur,
} from '../../interface/Classes';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { EventTypes } from '../../interface/event-type';

declare var $: any;

@Component({
  selector: 'app-offre',
  templateUrl: './offre.component.html',
  styleUrls: ['./offre.component.css'],
})
export class OffreComponent {
  appelsOffresPublie: AppelOffre[] = [];
  selectedAppelOffre: AppelOffre | null = null;
  selectedRessource: Imprimante | Ordinateur | undefined;
  ressourcesFournisseur: RessourceFournisseur[] = [];
  offresFournisseur: Offre[] = [];
  selectedSentAppelOffre: AppelOffre | null = null;
  userId: string | null = null;

  constructor(
    private appelOffreService: GestionAppelOffreService,
    private offreService: OffreService,
    private auth: AuthService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadAppelsOffre();
    this.loadRessourceFournisseurFromStorage();
    this.loadOffresFournisseur();
    this.userId = localStorage.getItem('userId')!;
  }

  loadAppelsOffre() {
    this.appelsOffresPublie = [];
    this.getAppelsOffre();
  }

  loadOffresFournisseur() {
    this.offreService.getOffresFournisseur(this.userId).subscribe({
      next: (data) => {
        this.offresFournisseur = data;
      },
      error: (error) => console.log(error),
    });
  }

  getAppelsOffre(): void {
    this.appelOffreService.getAllAppelOffre().subscribe({
      next: (data: AppelOffre[]) => {
        this.loadOffresFournisseur();
        const offreIds: number[] = [];
        this.offresFournisseur.forEach((offre) => {
          console.log('hello' + offre);

          offreIds.push(offre.idAppelOffre);
        });

        console.log(data);

        this.appelsOffresPublie = data
          .filter(
            (appelOffre) =>
              appelOffre.datePub != null ||
              (appelOffre.datePub != null &&
                offreIds.includes(appelOffre.id ?? -1))
          )
          .reverse();
        console.log(this.appelsOffresPublie);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#appelsOffreTable').DataTable();
      });
    }, 500);
  }

  selectAppelOffre(appelOffre: AppelOffre | null): void {
    this.selectedAppelOffre = appelOffre;
  }

  selectSentAppelOffre(appelOffre: AppelOffre | null): void {
    this.selectedSentAppelOffre = appelOffre;
  }

  public unSelectAppelOffre(): void {
    this.selectedAppelOffre = null;
  }

  public addRessourceToLocalStorage() {
    let ressourcesLocal = '';
    this.ressourcesFournisseur?.forEach((ress) => {
      ressourcesLocal += JSON.stringify(ress) + ';';
    });
    localStorage.setItem('ressources_fournisseur_local', ressourcesLocal);
  }

  public handleAddRessourceFournisseur(offreForm: NgForm): void {
    var ressourceFournisseur = {} as RessourceFournisseur;
    ressourceFournisseur.marque = offreForm.value.marque;
    ressourceFournisseur.prix = offreForm.value.prix;
    ressourceFournisseur.idRessource = offreForm.value.idRessource;
    this.ressourcesFournisseur.push(ressourceFournisseur);
    this.addRessourceToLocalStorage();
    offreForm.reset();
  }

  loadRessourceFournisseurFromStorage() {
    let ressourceFournisseurLocal = localStorage.getItem(
      'ressources_fournisseur_local'
    );
    let ressourceFournisseur = ressourceFournisseurLocal?.split(';');
    ressourceFournisseur?.forEach((ress) => {
      if (ress != '') this.ressourcesFournisseur?.push(JSON.parse(ress));
    });
  }

  public isRessourceInRessourceFournisseur(ress: Ressource): boolean {
    let isIn = false;
    this.ressourcesFournisseur.forEach((r) => {
      if (r.idRessource == ress.id) isIn = true;
    });
    return isIn;
  }

  public handleRemoveFromRessourceFournisseur(ress: Ressource) {
    this.ressourcesFournisseur = this.ressourcesFournisseur.filter(
      (r) => r.idRessource != ress.id
    );
    this.addRessourceToLocalStorage();
  }

  public selectRessource(ressource: Ordinateur | Imprimante): void {
    this.selectedRessource = ressource;
  }

  public handleEnvoyerOffre(dateForm: NgForm) {
    var offre = {} as Offre;
    offre.dateDebut = dateForm.value.dateDebut;
    offre.dateFin = dateForm.value.dateFin;
    offre.idAppelOffre = this.selectedAppelOffre?.id ?? -1;
    offre.isAffected = false;
    offre.isRejected = false;
    offre.isWaiting = true;
    offre.idFournisseur = this.userId;
    offre.ressources = this.ressourcesFournisseur;

    this.offreService.saveOffre(offre).subscribe({
      next: () => {
        this.ressourcesFournisseur = [];
        localStorage.removeItem('ressources_fournisseur_local');
        this.loadOffresFournisseur();
        this.toastService.showInfoToast(
          EventTypes.Info,
          'votre offre a été publié'
        );
      },
      error: (error) => console.log(error),
    });
  }

  public getOffreOfFournisseur(appelOffre: AppelOffre | null): Offre | null {
    var offre = {} as Offre;

    this.appelsOffresPublie.forEach((a) => {
      if (a.id == appelOffre?.id) {
        this.offresFournisseur.forEach((o) => {
          if (o.idAppelOffre == appelOffre?.id) offre = o;
        });
      }
    });
    // console.log(offre)
    return offre;
  }

  public isFournisseurInAppelOffre(appelOffre: AppelOffre | null): boolean {
    let isIn: boolean = false;
    this.offresFournisseur.forEach((offre) => {
      if (offre.idAppelOffre == appelOffre?.id) isIn == true;
    });

    return isIn;
  }
}
