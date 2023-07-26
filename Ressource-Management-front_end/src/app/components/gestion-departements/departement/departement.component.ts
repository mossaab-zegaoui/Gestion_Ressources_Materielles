import {Component} from '@angular/core';
import {Departement} from 'src/app/interface/Classes';
import {GestionDepartementsService} from '../../../services/gestion-departements.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {catchError, map, Observable, of, startWith} from "rxjs";
import {AppState} from "../../../interface/appstate";
import {DataState} from "../../../interface/datastate";

declare var $: any;

@Component({
  selector: 'app-departement',
  templateUrl: './departement.component.html',
  styleUrls: ['./departement.component.css'],
})
export class DepartementComponent {
  readonly DataState = DataState;
  nomDepartement: string | null = null;
  editDepartementForm: FormGroup = this.fb.group({
    id: [],
    nomDepartement: []
  })
  deleteDepartementForm: FormGroup = this.fb.group({
    id: [],
    nomDepartement: []
  })
  departements$: Observable<AppState<Departement[]>> | null = null;

  constructor(private departementService: GestionDepartementsService, private fb: FormBuilder) {
  }

  ngOnInit() {
    this.departements$ = this.getDepartements();
  }

  public handleAjouterDepartement(): void {

    this.departementService
      .addDepartement(this.nomDepartement).subscribe(() => {
        this.nomDepartement = ""
      this.departements$ = this.getDepartements()
    })
  }

  public handleEditDepartementForm(): void {
    this.departementService.editDepartement(this.editDepartementForm.value).subscribe(() => {
      this.departements$ = this.getDepartements();
    })
  }

  public handleDeleteDepartement(): void {
    this.departementService.deleteDepartement(this.deleteDepartementForm.get('id')?.value).subscribe(() => {
      this.departements$ = this.getDepartements();
    })
  }

  public openModal(departement: Departement, mode: string): void {
    if (mode == 'editDepartement') {
      this.editDepartementForm = this.fb.group({
        id: [departement.id],
        nomDepartement: [departement.nomDepartement]
      })
    } else {
      this.deleteDepartementForm = this.fb.group({
        id: [departement.id, Validators.required],
        nomDepartement: [departement.nomDepartement, Validators.required]
      })
    }

  }

  getDepartements() {
    return this.departementService.getAllDepartements().pipe(
      map((response: Departement[]) => {
        return {state: DataState.LOADED, data: response}
      }),
      startWith({state: DataState.LOADING}),
      catchError((err) => of({
        state: DataState.ERROR,
        error: err.message
      }))
    )
  }
}
