import { Component } from '@angular/core';
import { Departement, fournisseur, MembreDepartement, Ordinateur } from "../../../interface/Classes";
import { GestionDepartementsService } from "../../../services/gestion-departements.service";
import { OrdinateurService } from "../../../services/ordinateur.service";
import { HttpErrorResponse } from "@angular/common/http";
import { NgForm } from "@angular/forms";
declare var $: any;
@Component({
  selector: 'app-ordinateur',
  templateUrl: './ordinateur.component.html',
  styleUrls: ['./ordinateur.component.css']
})
export class OrdinateurComponent {
  listeOrdinateurNonLivre!: Array<Ordinateur>
  listeFournisseur!: Array<fournisseur>
  listeEnseignant!: Array<MembreDepartement>
  deleteOrdinateur!: Ordinateur | undefined
  editOrdinateur!: Ordinateur | undefined
  editFournisseur!: fournisseur | undefined
  ordinateursDisponibles!: Array<Ordinateur>
  isOrdinateursDispo!: boolean;

  constructor(private ordinateurService: OrdinateurService) { }

  ngOnInit(): void {
    this.loadOrdinateur();
    this.ngAfterViewInitDisponible();
  }
  ngAfterViewInitDisponible(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#ordinateurDisponible').DataTable();
      });
    }, 500);
  }
  public loadOrdinateur() {
    this.listeFournisseur = [];
    this.getFournisseur();
    this.listeEnseignant = [];
    this.getEnseignant();
    this.ordinateursDisponibles = [];
    this.getOrdinateurDisponible();
    this.listeOrdinateurNonLivre = [];
    this.getOrdinateurNonLivre();


  }

  public getOrdinateurNonLivre(): void {
    this.ordinateurService.getOrdinateurNonLivre().subscribe({
      next: (data: Ordinateur[]) => {
        this.listeOrdinateurNonLivre = data;
        this.ngAfterViewInitNonLivre();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
    this.isOrdinateursDispo = false;
  }
  public getOrdinateurDisponible(): void {
    this.ordinateurService.getOrdinateurDisponible().subscribe({
      next: (data: Ordinateur[]) => {
        this.ordinateursDisponibles = data;
        this.ngAfterViewInitDisponible();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
    this.isOrdinateursDispo = true;
  }

  public getFournisseur(): void {
    this.ordinateurService.getFournisseur().subscribe({
      next: (data: fournisseur[]) => {
        this.listeFournisseur = data;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
  }

  public getEnseignant(): void {
    this.ordinateurService.getEnseignant().subscribe({
      next: (data: MembreDepartement[]) => {
        this.listeEnseignant = data;

      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      }
    })
  }

  public getFournisseurNom(id: String | null): String {
    return this.listeFournisseur.filter(p => p.id == id)[0].username;
  }

  public getEnseignantNom(id: string | null): string {
    const enseignant = this.listeEnseignant.find(p => p.id === id) ?? { nom: '', prenom: '' };
    return enseignant.nom + ' ' + enseignant.prenom;
  }
  public getNomSociete(id: String | null): String {
    return this.listeFournisseur.filter(p => p.id == id)[0].nomSociete;
  }

  public handleDeleteOrdinateur(): void {
    if (this.deleteOrdinateur != undefined) {

      this.ordinateurService.deleteOrdinateur(this.deleteOrdinateur!.id).subscribe({

        next: () => {
          let index = this.listeOrdinateurNonLivre.indexOf(this.deleteOrdinateur!);
          this.listeOrdinateurNonLivre.splice(index, 1);
        },
        error: (error) => console.log(error)
      });
    }
  }

  public openModal(ordinateur: Ordinateur, mode: string): void {
    if (mode == "editOrdinateur") {
      this.editOrdinateur = ordinateur
      this.editFournisseur = this.listeFournisseur.filter(p => p.id == this.editOrdinateur?.idFournisseur)[0]
    }
    else
      if (mode == "deleteOrdinateur")
        this.deleteOrdinateur = ordinateur;
  }

  public handleAjouterOrdinateur(ajouterRessourceForm: NgForm): void {
    this.affectationOrdinateur(ajouterRessourceForm);

    this.ordinateurService.modifierOrdinateur(this.editOrdinateur).subscribe({
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

    this.ordinateurService.modifierFournisseur(this.editFournisseur).subscribe({
      next: (data) => {
        this.ngOnInit()
      },
      error: (error) => {
        console.log(error);
      }
    })


  }


  private affectationOrdinateur(ajouterRessourceForm: NgForm) {
    this.editOrdinateur!.codeBarre = ajouterRessourceForm.value.codebarre
    this.editOrdinateur!.ram = ajouterRessourceForm.value.ram
    this.editOrdinateur!.cpu = ajouterRessourceForm.value.cpu
    this.editOrdinateur!.disqueDur = ajouterRessourceForm.value.disquedur
    this.editOrdinateur!.dateLivraison = ajouterRessourceForm.value.datelivraison
    this.editOrdinateur!.dateFinGarantie = ajouterRessourceForm.value.datefingarantie
    this.editOrdinateur!.ecran = ajouterRessourceForm.value.ecran
    this.editOrdinateur!.prix = ajouterRessourceForm.value.prix
    this.editOrdinateur!.marque = ajouterRessourceForm.value.marque
    this.editOrdinateur!.idMembreDepartement = ajouterRessourceForm.value.enseignantid
  }

  ngAfterViewInitNonLivre(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#ordinateurNonLivre').DataTable();
      });
    }, 500);
  }



}
