import { Component, OnInit } from '@angular/core';
import { Besoin, Departement } from 'src/app/interface/Classes';
import { EventTypes } from 'src/app/interface/event-type';
import { GestionBesoinsService } from 'src/app/services/gestion-besoins.service';
import { GestionDepartementsService } from 'src/app/services/gestion-departements.service';
import { ToastService } from 'src/app/services/toast.service';
declare var $: any;

@Component({
  selector: 'app-liste-besoin',
  templateUrl: './liste-besoin.component.html',
  styleUrls: ['./liste-besoin.component.css']
})
export class ListeBesoinComponent implements OnInit {

  public listeBesoinsByDepartement: Besoin[] = [];
  public departements: Departement[] = [];
  public selectedBesoin!: Besoin | undefined;
  userId!: string;
  idDepartement!: number;
  public constructor(private gestionBesoinsService: GestionBesoinsService,
    private gestionDepartementsService: GestionDepartementsService,
    private toastService: ToastService) { }

  ngOnInit(): void {
    this.getBesoinsByDepartement();
    this.getDepartements();
    this.userId = localStorage.getItem('userId')!;
    this.idDepartement = Number(localStorage.getItem("departementId"));

  }

  public getBesoinsByDepartement() {
    let idDepartement = Number(localStorage.getItem("departementId"));
    this.gestionBesoinsService.getBesoinsByIdDepartement(idDepartement).subscribe({
      next: (data) => {
        console.log(data);
        this.listeBesoinsByDepartement = data.reverse();
      },
      error: (error) => console.log(error)
    })
  }

  public getDepartements(): void {
    this.gestionDepartementsService.getAllDepartements().subscribe({
      next: (data: Departement[]) => {
        this.departements = data;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  public getDepartementName(idDepartement: number): string | undefined {
    const departement = this.departements.find(dep => dep.id === idDepartement);
    return departement?.nomDepartement;
  }

  public showRessourcesBesoin(besoin: Besoin) {
    this.selectedBesoin = besoin;
  }

  public sendBesoinsRequest() {
    let idDepartement = Number(localStorage.getItem("departementId"));
    this.gestionBesoinsService.sendBesoinsRequest(idDepartement).subscribe({
      next: () => {
        this.toastService.showSuccessToast(EventTypes.Success, "Demande Besoins a été envoyé");
      },
      error: (error) => console.log(error)
    })
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#listeBesoinTable').DataTable();
      });
    }, 500);
  }

}
