import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { Departement, Technicien } from 'src/app/interface/Classes';
import { GestionPannesService } from 'src/app/services/gestion-pannes.service';
declare var $: any;
@Component({
  selector: 'app-technicien',
  templateUrl: './technicien.component.html',
  styleUrls: ['./technicien.component.css'],
})
export class TechnicienComponent {
  public techniciens!: Technicien[];
  deleteTechnicien!: Technicien | undefined;
  editTechnicien!: Technicien | undefined;
  addTechnicienForm!: FormGroup;
  public nomInvalid = false;
  public prenomInvalid = false;
  public usernameInvalid = false;
  public passwordInvalid = false;
  public emailInvalid = false;
  public cinInvalid = false;
  public specialiteInvalid = false;

  public constructor(private gestionPannesService: GestionPannesService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.loadTechnicien();

    this.addTechnicienForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
      specialite: ['', Validators.required]
    }
    )
  }

  public loadTechnicien() {
    this.techniciens = [];
    this.getAllTechnicien();
  }

  public getAllTechnicien() {
    this.gestionPannesService.getAllTechniciens().subscribe({
      next: (data) => {
        this.techniciens = data;
      },
      error: (error) => console.log(error),
    });
  }


  public handleAjouterTechnicien(): void {
    if (!this.addTechnicienForm.valid)
      this.validateAllFormsFields(this.addTechnicienForm);
    else {
      this.gestionPannesService.addTechnicien(this.addTechnicienForm.value).subscribe({
        next: (data) => {
          this.techniciens.push(data);
          this.addTechnicienForm.reset();
          console.log(data);
        },
        error: (error) => console.log(error)
      });
    }

  }

  public handleDeleteTechnicien() {
    if (this.deleteTechnicien != undefined) {
      this.gestionPannesService.deleteTechnicien(this.deleteTechnicien!.id).subscribe({
        next: () => {
          this.getAllTechnicien();
        },
        error: (error) => console.log(error),
      });
      this.deleteTechnicien = undefined;
    }
  }

  public handleEditTechnicien(technicien: Technicien) {

    const updatedTechicien = { ...this.editTechnicien, ...technicien };
    console.warn(updatedTechicien);
    this.gestionPannesService.editTechnicien(technicien).subscribe({
      next: () => {
        this.loadTechnicien();
        this.addTechnicienForm.reset();

      },
      error: (error) => console.log(error),
    });
  }

  public openModal(technicien: Technicien, mode: string): void {
    if (mode == 'editTechnicien') this.editTechnicien = technicien;
    else if (mode == 'deleteTechnicien') this.deleteTechnicien = technicien;
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      $(document).ready(function () {
        $('#techniciensTable').DataTable();
      });
    }, 500);
  }
  private validateAllFormsFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormsFields(control)
      }
    })
  }
}
