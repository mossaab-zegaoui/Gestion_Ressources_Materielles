import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import {
  Departement,
  MembreDepartement,
  Role,
} from 'src/app/interface/Classes';
import { GestionDepartementsService } from 'src/app/services/gestion-departements.service';
import {
  BehaviorSubject,
  catchError,
  map,
  Observable,
  of,
  startWith,
} from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { AppState } from '../../../interface/appstate';
import { DataState } from '../../../interface/datastate';

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
  selectedMembreId: string | undefined = undefined;
  membresSubject = new BehaviorSubject<MembreDepartement[]>([]);
  departementsSubject = new BehaviorSubject<Departement[]>([]);
  rolesSubject = new BehaviorSubject<Role[]>([]);

  addMembreForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    email: ['', Validators.required],
    roles: ['', Validators.required],
    departement: ['', Validators.required],
    prenom: ['', Validators.required],
    nom: ['', Validators.required],
  });
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
  ) {}

  ngOnInit(): void {
    this.membres$ = this.gestionDepartementsService.getAllMembers().pipe(
      map((membres: MembreDepartement[]) => {
        this.membresSubject.next(membres);
        return { state: DataState.LOADED, data: this.membresSubject.value };
      }),
      startWith({ state: DataState.LOADING }),
      catchError((error) =>
        of({
          dataState: DataState.ERROR,
          error,
        })
      )
    );
    this.departements$ = this.auth.departements$().pipe(
      map((departements: Departement[]) => {
        this.departementsSubject.next(departements);
        return {
          state: DataState.LOADED,
          data: this.departementsSubject.value,
        };
      }),
      startWith({ state: DataState.LOADING }),
      catchError((error) =>
        of({
          dataState: DataState.ERROR,
          error,
        })
      )
    );
    this.roles$ = this.auth.roles$().pipe(
      map((response) => {
        this.rolesSubject.next(response);
        return { state: DataState.LOADED, data: this.rolesSubject.value };
      }),
      startWith({ state: DataState.LOADING }),
      catchError((error) =>
        of({
          state: DataState.ERROR,
          error,
        })
      )
    );
  }

  handleAjouterMembre(): void {
    const membre: MembreDepartement = {
      ...this.addMembreForm.value,
      roles: [{ nomRole: this.addMembreForm.value.roles }],
    };
    this.membres$ = this.gestionDepartementsService.addMembre(membre).pipe(
      map((membre: MembreDepartement) => {
        this.membresSubject.next([...this.membresSubject.value, membre]);
        this.addMembreForm.reset();
        return { state: DataState.LOADED, data: this.membresSubject.value };
      }),
      startWith({
        state: DataState.LOADING,
        data: this.membresSubject.value,
      }),
      catchError((error) =>
        of({
          state: DataState.ERROR,
          data: this.membresSubject.value,
          error,
        })
      )
    );
  }

  updateMembre() {
    this.membres$ = this.gestionDepartementsService
      .updateMembre(this.updateForm.value)
      .pipe(
        map((response: MembreDepartement) => {
          const membres = this.membresSubject.value.map((membre) =>
            membre.id === response.id ? response : membre
          );
          this.membresSubject.next(membres);
          return { state: DataState.LOADED, data: this.membresSubject.value };
        }),
        startWith({
          state: DataState.LOADING,
          data: this.membresSubject.value,
        }),
        catchError((error) =>
          of({
            state: DataState.ERROR,
            data: this.membresSubject.value,
            error,
          })
        )
      );
  }

  handleDeleteMembre() {
    this.membres$ = this.gestionDepartementsService
      .deleteMembre(this.selectedMembreId)
      .pipe(
        map(() => {
          const membres = this.membresSubject.value.filter(
            (membre) => membre.id !== this.selectedMembreId
          );
          this.membresSubject.next(membres);
          return { state: DataState.LOADED, data: this.membresSubject.value };
        }),
        startWith({
          state: DataState.LOADING,
          data: this.membresSubject.value,
        }),
        catchError((error) =>
          of({
            state: DataState.ERROR,
            data: this.membresSubject.value,
            error,
          })
        )
      );
  }

  openModal(membre: MembreDepartement | undefined, mode: string): void {
    if (mode == 'deleteMembre') this.selectedMembreId = membre?.id;
    else {
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
    }
  }

  private validateAllFormsFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((field) => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormsFields(control);
      }
    });
  }
}
