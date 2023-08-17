import { Component } from '@angular/core';
import { Departement } from 'src/app/interface/Classes';
import { GestionDepartementsService } from '../../../services/gestion-departements.service';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import {
  BehaviorSubject,
  catchError,
  map,
  Observable,
  of,
  startWith,
} from 'rxjs';
import { AppState } from '../../../interface/appstate';
import { DataState } from '../../../interface/datastate';

declare var $: any;

@Component({
  selector: 'app-departement',
  templateUrl: './departement.component.html',
  styleUrls: ['./departement.component.css'],
})
export class DepartementComponent {
  readonly DataState = DataState;
  addDepartementForm: FormGroup = this.fb.group({
    nomDepartement: [],
  });

  editDepartementForm: FormGroup = this.fb.group({
    id: [],
    nomDepartement: [],
  });
  deleteDepartementForm: FormGroup = this.fb.group({
    id: [],
    nomDepartement: [],
  });
  departementsSubject = new BehaviorSubject<Departement[]>([]);
  departements$: Observable<AppState<Departement[]>> | null = null;

  constructor(
    private departementService: GestionDepartementsService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.departements$ = this.departementService.getAllDepartements().pipe(
      map((departments: Departement[]) => {
        this.departementsSubject.next(departments);
        return {
          state: DataState.LOADED,
          data: this.departementsSubject.value,
        };
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

  public handleAjouterDepartement(): void {
    this.departements$ = this.departementService
      .addDepartement(this.addDepartementForm.value.nomDepartement)
      .pipe(
        map((departement: Departement) => {
          this.departementsSubject.next([
            ...this.departementsSubject.value,
            departement,
          ]);
          this.addDepartementForm.reset();
          return {
            state: DataState.LOADED,
            data: this.departementsSubject.value,
          };
        }),
        startWith({
          dataState: DataState.LOADING,
          data: this.departementsSubject.value,
        }),
        catchError((error) =>
          of({
            state: DataState.ERROR,
            data: this.departementsSubject.value,
            error,
          })
        )
      );
  }

  public handleEditDepartementForm(): void {
    this.departements$ = this.departementService
      .editDepartement(this.editDepartementForm.value)
      .pipe(
        map((response: Departement) => {
          const departements = this.departementsSubject.value.map(
            (departement) =>
              departement.id === response.id ? response : departement
          );
          this.departementsSubject.next(departements);
          return {
            state: DataState.LOADED,
            data: this.departementsSubject.value,
          };
        }),
        startWith({
          state: DataState.LOADING,
          data: this.departementsSubject.value,
        }),
        catchError((error) =>
          of({
            state: DataState.ERROR,
            data: this.departementsSubject.value,
            error,
          })
        )
      );
  }

  public handleDeleteDepartement(): void {
    const departementId = this.deleteDepartementForm.get('id')?.value;
    this.departements$ = this.departementService
      .deleteDepartement(departementId)
      .pipe(
        map(() => {
          const departements = this.departementsSubject.value.filter(
            (departement) => departement.id !== departementId
          );
          this.departementsSubject.next(departements);
          return {
            state: DataState.LOADED,
            data: this.departementsSubject.value,
          };
        }),
        startWith({
          state: DataState.LOADING,
          data: this.departementsSubject.value,
        }),
        catchError((error) =>
          of({
            state: DataState.ERROR,
            data: this.departementsSubject.value,
            error,
          })
        )
      );
  }

  public openModal(departement: Departement, mode: string): void {
    if (mode == 'editDepartement') {
      this.editDepartementForm = this.fb.group({
        id: [departement.id],
        nomDepartement: [departement.nomDepartement],
      });
    } else {
      this.deleteDepartementForm = this.fb.group({
        id: [departement.id, Validators.required],
        nomDepartement: [departement.nomDepartement, Validators.required],
      });
    }
  }
}
