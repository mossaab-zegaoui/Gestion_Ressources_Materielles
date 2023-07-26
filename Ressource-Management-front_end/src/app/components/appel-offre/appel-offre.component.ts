import { Component } from '@angular/core';
import {
  AppelOffre,
  Besoin,
  Departement,
  Imprimante,
  MembreDepartement,
  Offre,
  Ordinateur,
  RessourceFournisseur,
} from '../../interface/Classes';
import { GestionAppelOffreService } from '../../services/gestion-appel-offre.service';
import { GestionBesoinsService } from '../../services/gestion-besoins.service';
import { GestionDepartementsService } from '../../services/gestion-departements.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { OffreService } from '../../services/offre.service';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { EventTypes } from '../../interface/event-type';
declare var $: any;

@Component({
  selector: 'app-appel-offre',
  templateUrl: './appel-offre.component.html',
  styleUrls: ['./appel-offre.component.css'],
})
export class AppelOffreComponent {
  public appelsOffre: AppelOffre[] = [];
  public besoinsNotInAppelOffre: Besoin[] = [];
  public selectedBesoin: Besoin | undefined;
  public deletedAppelOffre!: AppelOffre | null;
  public offresOfAppel!: Offre[];
  public selecedtOffre: Offre | undefined;
  public departements: Departement[] = [];
  public membresDep: MembreDepartement[] = [];
  public blackListOpened: boolean = false;
  selectAll: boolean = false;

  constructor(
    private appelOffreService: GestionAppelOffreService,
    private besoinService: GestionBesoinsService,
    private offreService: OffreService,
    private gestionDepartementsService: GestionDepartementsService,
    private auth: AuthService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.getDepartements();
    this.getAllMembresDepartement();
    this.loadAppelsOffre();
    this.loadBesoinsNotInAppelOffre();
  }

  public hasRole(role: string[]): boolean {
    const roles: string[] = this.auth.decodedToken().ROLES.map(
      (role: { authority: string }) => role.authority
    );
    return roles.some((item) => role.includes(item));
  }
  loadAppelsOffre() {
    this.appelsOffre = [];
    this.getAppelsOffre();
  }

  public getDepartements(): void {
    this.gestionDepartementsService.getAllDepartements().subscribe({
      next: (data: Departement[]) => {
        this.departements = data;
        this.ngAfterViewInit();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getAllMembresDepartement() {
    this.gestionDepartementsService.getAllMembers().subscribe({
      next: (data) => {
        this.membresDep = data;
      },
      error: (error) => console.log(error),
    });
  }

  public getMembreDepartement(
    idMembre: string | null | undefined
  ): MembreDepartement | undefined {
    return this.membresDep.filter((mem) => mem.id == idMembre)[0];
  }

  public blackListFournisseur(motif: string) {
    this.blackListOpened = false;
    let idFournisseur = this.selecedtOffre?.idFournisseur;
    this.appelOffreService
      .blackListFournisseur(idFournisseur, motif)
      .subscribe({
        next: () => {
          console.log('Banned');
        },
        error: (error) => console.log(error),
      });
  }

  loadBesoinsNotInAppelOffre(): void {
    this.besoinService.getBesoinsNotInAppelOffre().subscribe({
      next: (data: Besoin[]) => {
        this.besoinsNotInAppelOffre = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  getDatePubAppelOffre(appelOffre: AppelOffre): string | null | Date {
    return appelOffre.datePub;
  }
  getAppelsOffre(): void {
    this.appelOffreService.getAllAppelOffre().subscribe({
      next: (data: AppelOffre[]) => {
        this.appelsOffre = data;
        this.ngAfterViewInit();
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

  handleCreerOffre(): void {
    this.loadBesoinsNotInAppelOffre();

    if (!this.besoinsNotInAppelOffre.some(besoin => besoin.isSelected)) {
      this.toastService.showInfoToast(EventTypes.Info, 'Sélectionnez un besoin');
      return;
    }

    const currentAppelOffre: AppelOffre = {
      id: this.appelsOffre.length + 1,
      datePub: null,
      isAffected: false,
      besoins: this.besoinsNotInAppelOffre.filter(besoin => besoin.isSelected)
    };
    this.appelsOffre.push(currentAppelOffre);
  }

  handlePublierAppelOffre(appelOffre: AppelOffre): void {
    this.appelOffreService.publierAppelOffre(appelOffre).subscribe({
      next: () => {
        this.toastService.showSuccessToast(
          EventTypes.Success,
          "votre appel d'offre a été publiée"
        );
        this.loadAppelsOffre();
        this.loadBesoinsNotInAppelOffre();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }
  toogleSelectedAllBesoins(): void {
    this.besoinsNotInAppelOffre.forEach(
      (besoin) => (besoin.isSelected = this.selectAll)
    );
  }

  toggleSelection(): void {
    this.selectAll = this.besoinsNotInAppelOffre.every(
      (besoin) => besoin.isSelected
    );
  }

  handleAnnulerAppelOffre(appelOffre: AppelOffre | null): void {
    if (appelOffre) {
      const index = this.appelsOffre.indexOf(appelOffre);
      if (index !== -1) this.appelsOffre.splice(index, 1);
    }
  }

  getOffreOfAppel(appelOffre: AppelOffre) {
    this.offreService.getOffreByAppelOffre(appelOffre).subscribe({
      next: (data: Offre[]) => {
        this.offresOfAppel = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public selectBesoin(besoin: Besoin): void {
    this.selectedBesoin = besoin;
    console.warn(this.selectBesoin);
  }
  public selectOffre(offre: Offre): void {
    this.selecedtOffre = offre;
  }

  deleteModal(appelOffre: AppelOffre): void {
    this.deletedAppelOffre = appelOffre;
  }

  public getOrdinateurs(): Ordinateur[] {
    return this.selectedBesoin?.ordinateurs || [];
  }

  public getImprimantes(): Imprimante[] {
    return this.selectedBesoin?.imprimantes || [];
  }

  public accepterOffre(offre: Offre | undefined): void {
    this.offreService.accepterOffre(offre).subscribe({
      next: () => {
        this.ngAfterViewInit();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }
}
