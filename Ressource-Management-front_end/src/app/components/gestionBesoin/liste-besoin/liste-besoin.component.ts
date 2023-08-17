import { Component, OnInit } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  catchError,
  map,
  of,
  startWith,
} from 'rxjs';
import { Besoin, Departement } from 'src/app/interface/Classes';
import { DefaultState } from 'src/app/interface/appstate';
import { DataState } from 'src/app/interface/datastate';
import { EventTypes } from 'src/app/interface/event-type';
import { GestionBesoinsService } from 'src/app/services/gestion-besoins.service';
import { GestionDepartementsService } from 'src/app/services/gestion-departements.service';
import { ToastService } from 'src/app/services/toast.service';
declare var $: any;

@Component({
  selector: 'app-liste-besoin',
  templateUrl: './liste-besoin.component.html',
  styleUrls: ['./liste-besoin.component.css'],
})
export class ListeBesoinComponent implements OnInit {
  public selectedBesoin!: Besoin | undefined;
  besoinState$: Observable<DefaultState<Besoin[]>> | null = null;
  departements$: Observable<Departement[]> | null = null;
  besoinsSubject = new BehaviorSubject<Besoin[]>([]);
  public constructor(
    private gestionBesoinsService: GestionBesoinsService,
    private gestionDepartementsService: GestionDepartementsService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.departements$ = this.gestionDepartementsService.getAllDepartements();

    this.besoinState$ = this.gestionBesoinsService
      .getBesoinsByIdDepartement()
      .pipe(
        map((besoins) => {
          this.besoinsSubject.next(besoins);
          return { state: DataState.LOADED, data: besoins };
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

  public showRessourcesBesoin(besoin: Besoin) {
    this.selectedBesoin = besoin;
  }

  public sendBesoinsRequest() {
    this.besoinState$ = this.gestionBesoinsService.sendBesoinsRequest().pipe(
      map(() => {
        this.toastService.showSuccessToast(
          EventTypes.Success,
          'Demande Besoins a été envoyé'
        );
        return { state: DataState.LOADED, data: this.besoinsSubject.value };
      }),
      startWith({ state: DataState.LOADING, data: this.besoinsSubject.value }),
      catchError((error) =>
        of({
          state: DataState.ERROR,
          data: this.besoinsSubject.value,
          error,
        })
      )
    );
  }
}
