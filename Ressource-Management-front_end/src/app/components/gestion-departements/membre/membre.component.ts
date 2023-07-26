import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, NgForm, Validators,} from '@angular/forms';
import {Departement, MembreDepartement, Role} from 'src/app/interface/Classes';
import {GestionDepartementsService} from 'src/app/services/gestion-departements.service';
import {catchError, map, Observable, of, startWith} from "rxjs";
import {AuthService} from "../../../services/auth.service";
import {AppState} from "../../../interface/appstate";
import {DataState} from "../../../interface/datastate";

declare var $: any;

@Component({
  selector: 'app-membre',
  templateUrl: './membre.component.html',
  styleUrls: ['./membre.component.css'],
})
export class MembreComponent {
  readonly DataState = DataState;
  roles$: Observable<AppState<Role[]>> | null = null;
  departements$: Observable<AppState<Departement[]>> | null = null;
  membres$: Observable<AppState<MembreDepartement[]>> | null = null;
  membreId: string | undefined = undefined;
  addMembreForm!: FormGroup;
  updateForm: FormGroup = this.fb.group({
    id: [''],
    password: [''],
    username: ['', Validators.required],
    email: ['', Validators.required],
    roles: ['', Validators.required],
    departement: ['', Validators.required],
    prenom: ['', Validators.required],
    nom: ['', Validators.required],
  });

  public constructor(
    private gestionDepartementsService: GestionDepartementsService,
    private fb: FormBuilder,
    private auth: AuthService
  ) {
  }

  ngOnInit(): void {
    this.addMembreForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
      role: ['', Validators.required],
      departement: ['', Validators.required],
      prenom: ['', Validators.required],
      nom: ['', Validators.required],
    });
    this.membres$ = this.getMembres();
    this.departements$ = this.getDepartements();
    this.roles$ = this.auth.roles$().pipe(
      map((roles: Role[]) => {
        return {state: DataState.LOADED, data: roles}
      }),
      startWith({state: DataState.LOADING}),
      catchError((err) => of({
        dataState: DataState.ERROR, error: err.message
      }))
    );
  }

  private getDepartements() {
    return this.auth.departements$().pipe(
      map((departements: Departement[]) => {
        return {state: DataState.LOADED, data: departements}
      }),
      startWith({state: DataState.LOADING}),
      catchError((err) => of({
        dataState: DataState.ERROR, error: err.message
      }))
    );
  }

  private getMembres() {
    return this.gestionDepartementsService.getAllMembers().pipe(
      map((membres: MembreDepartement[]) => {
        return {state: DataState.LOADED, data: membres}
      }),
      startWith({state: DataState.LOADING}),
      catchError((err) => of({
        dataState: DataState.ERROR, error: err.message
      })));
  }

  handleAjouterMembre(): void {
    let membre: MembreDepartement = {
      "departement": {
        id: this.addMembreForm.get('departement')?.value.id,
        nomDepartement: this.addMembreForm.get('departement')?.value.nomDepartement
      },
      "email": this.addMembreForm.get('email')?.value,
      "nom": this.addMembreForm.get('nom')?.value,
      "password": this.addMembreForm.get('password')?.value,
      "prenom": this.addMembreForm.get('prenom')?.value,
      "roles": [this.addMembreForm.get('role')?.value],
      "username": this.addMembreForm.get('username')?.value
    }
    this.gestionDepartementsService
      .addMembre(membre)
      .subscribe({
        next: (response: MembreDepartement) => {
          this.addMembreForm.reset();
          this.membres$ = this.getMembres();
        },
        error: (error) => console.log(error),
      });
  }

  updateMembre(form: FormGroup) {
    this.gestionDepartementsService.updateMembre(form.value).subscribe({
      next: () => {
        this.membres$ = this.getMembres();
        this.departements$ = this.departements$;
      },
      error: (err) => {
        console.log(err.message)
      }
    })
  }

  handleDeleteMembre() {
    this.gestionDepartementsService.deleteMembre(this.membreId).subscribe({
      next: () => {
        this.membres$ = this.getMembres();
        this.departements$ = this.getDepartements();
      },
      error: (error) => console.log(error),
    });
  }


  openModal(membre: MembreDepartement | undefined, mode: string): void {
    if (mode == 'editMembre') {
      this.updateForm = this.fb.group({
        id: [membre?.id],
        password: [membre?.password],
        username: [membre?.username, Validators.required],
        email: [membre?.email, Validators.required],
        roles: [membre?.roles, Validators.required],
        departement: [membre?.departement, Validators.required],
        prenom: [membre?.prenom, Validators.required],
        nom: [membre?.nom, Validators.required],
      });

    } else this.membreId = membre?.id;
  }

  private validateAllFormsFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((field) => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({onlySelf: true});
      } else if (control instanceof FormGroup) {
        this.validateAllFormsFields(control);
      }
    });
  }
}
