import { Component } from '@angular/core';
import { fournisseur, Imprimante, MembreDepartement } from "../../../interface/Classes";
import { HttpErrorResponse } from "@angular/common/http";
import { NgForm } from "@angular/forms";
import { ImprimanteService } from "../../../services/imprimante.service";
declare var $: any;

@Component({
  selector: 'app-imprimante',
  templateUrl: './imprimante.component.html',
  styleUrls: ['./imprimante.component.css']
})
export class ImprimanteComponent {

  listeImprimanteNonLivre!: Array<Imprimante>
  listeFournisseur!: Array<fournisseur>
  listeEnseignant!: Array<MembreDepartement>
  deleteImprimante!: Imprimante | undefined
  editImprimante!: Imprimante | undefined
  editFournisseur!: fournisseur | undefined
  listeImprimanteDisponible!: Array<Imprimante>
  ordinateur: any;
  isImprimantesDispo!: boolean;

  constructor(private imprimanteService: ImprimanteService) { }

  ngOnInit(): void {
    this.loadImprimante();
    this.ngAfterViewInitNonLivre();
  }

  public loadImprimante() {
    this.listeFournisseur = [];
    this.getFournisseur()
    this.listeEnseignant = [];
    this.getEnseignant();
    this.listeImprimanteDisponible = [];
    this.getImprimanteDisponible();
    this.listeImprimanteNonLivre = [];
    this.getImprimanteNonLivre();


  }

  public getImprimanteNonLivre(): void {
    this.imprimanteService.getImprimanteNonLivre().subscribe({
      next: (data: Imprimante[]) => {
        this.listeImprimanteNonLivre = data;
        this.ngAfterViewInitNonLivre();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
    this.isImprimantesDispo = false;
  }
  public getImprimanteDisponible(): void {
    this.imprimanteService.getImprimanteDisponible().subscribe({
      next: (data: Imprimante[]) => {
        this.listeImprimanteDisponible = data;
        this.ngAfterViewInitDisponible();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
    this.isImprimantesDispo = true;
  }

  public getFournisseur(): void {
    this.imprimanteService.getFournisseur().subscribe({
      next: (data: fournisseur[]) => {
        this.listeFournisseur = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
  }

  public getEnseignant(): void {
    this.imprimanteService.getEnseignant().subscribe({
      next: (data: MembreDepartement[]) => {
        this.listeEnseignant = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
  }

  public getFournisseurNom(id: String | null): String {
    return this.listeFournisseur.find(p => p.id == id)!.prenom;
  }

  public getEnseignantNom(id: String | null): String {

    return this.listeEnseignant.find(p => p.id == id)!.prenom;
  }
  public getNomSociete(id: String | null): String {
    return this.listeFournisseur.find(p => p.id == id)!.nomSociete;
  }

  public handleDeleteImprimante(): void {
    if (this.deleteImprimante != undefined) {

      this.imprimanteService.deleteImprimante(this.deleteImprimante!.id).subscribe({

        next: () => {
          let index = this.listeImprimanteNonLivre.indexOf(this.deleteImprimante!);
          this.listeImprimanteNonLivre.splice(index, 1);
        },
        error: (error) => console.log(error)
      });
    }
  }

  public openModal(imprimante: Imprimante, mode: string): void {
    if (mode == "editImprimante") {
      this.editImprimante = imprimante
      this.editFournisseur = this.listeFournisseur.find(p => p.id == this.editImprimante?.idFournisseur)!
    }
    else
      if (mode == "deleteImprimante")
        this.deleteImprimante = imprimante;
  }


  ngAfterViewInitDisponible(): void {
    // $(document).ready(function() {
    //   $('#departementsTable').DataTable();
    // });
    setTimeout(() => {
      $(document).ready(function () {
        $('#imprimanteDisponible').DataTable();
      });
    }, 500);
  }

  public handleAjouterOrdinateur(ajouterRessourceForm: NgForm): void {
    this.affectationImprimante(ajouterRessourceForm);

    this.imprimanteService.modifierImprimante(this.editImprimante).subscribe({
      next: (data) => {
        this.ngOnInit()
      },
      error: (error) => {
        console.log(error);
      }
    })

    this.editFournisseur!.nomSociete = ajouterRessourceForm.value.NomSociete
    this.editFournisseur!.addresse = ajouterRessourceForm.value.Adresse
    this.editFournisseur!.email = ajouterRessourceForm.value.Email
    this.editFournisseur!.gerant = ajouterRessourceForm.value.Gerant
    this.editFournisseur!.cin = ajouterRessourceForm.value.cin
    this.editFournisseur!.prenom = ajouterRessourceForm.value.Prénom
    this.editFournisseur!.nom = ajouterRessourceForm.value.Nom

    this.imprimanteService.modifierFournisseur(this.editFournisseur).subscribe({
      next: (data) => {
        this.ngOnInit()
      },
      error: (error) => {
        console.log(error);
      }
    })


  }


  private affectationImprimante(ajouterRessourceForm: NgForm) {
    this.editImprimante!.codeBarre = ajouterRessourceForm.value.codebarre
    this.editImprimante!.resolution = ajouterRessourceForm.value.resolution
    this.editImprimante!.vitesseImpression = ajouterRessourceForm.value.vitesseimpression
    this.editImprimante!.dateLivraison = ajouterRessourceForm.value.datelivraison
    this.editImprimante!.dateFinGarantie = ajouterRessourceForm.value.datefingarantie
    this.editImprimante!.prix = ajouterRessourceForm.value.prix
    this.editImprimante!.marque = ajouterRessourceForm.value.marque
    this.editImprimante!.idMembreDepartement = ajouterRessourceForm.value.enseignantid
  }

  ngAfterViewInitNonLivre(): void {
    // $(document).ready(function() {
    //   $('#departementsTable').DataTable();
    // });
    setTimeout(() => {
      $(document).ready(function () {
        $('#imprimanteNonLivre').DataTable();
      });
    }, 500);
  }



}
