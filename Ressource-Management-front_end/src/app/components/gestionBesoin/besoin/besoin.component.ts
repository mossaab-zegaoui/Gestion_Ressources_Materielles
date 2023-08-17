import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Besoin, Imprimante, Ordinateur } from 'src/app/interface/Classes';
import { AuthService } from 'src/app/services/auth.service';
import { GestionBesoinsService } from 'src/app/services/gestion-besoins.service';
import {
  RAM,
  CPU,
  ECRAN,
  DISQUE,
  VITESSEIMP,
  RESOLUTIONIMP,
} from 'src/app/interface/Consts';
import { ToastService } from 'src/app/services/toast.service';
import { EventTypes } from 'src/app/interface/event-type';
import {
  BehaviorSubject,
  Observable,
  catchError,
  map,
  of,
  startWith,
} from 'rxjs';
import { AppState, DefaultState } from 'src/app/interface/appstate';
import { DataState } from 'src/app/interface/datastate';

@Component({
  selector: 'app-besoin',
  templateUrl: './besoin.component.html',
  styleUrls: ['./besoin.component.css'],
})
export class BesoinComponent {
  [x: string]: any;

  type: string = '';
  openAddBesoin: boolean = false;
  isForDepartement: boolean = false;
  besoinState$: Observable<DefaultState<Besoin>> | null = null;
  besoin = new BehaviorSubject<Besoin>({ ordinateurs: [], imprimantes: [] });
  besoin$ = this.besoin.asObservable();
  cpus = CPU;
  rams = RAM;
  ecran = ECRAN;
  disques = DISQUE;
  vitesseimp = VITESSEIMP;
  resolutionimp = RESOLUTIONIMP;
  public constructor(
    private gestionBesoinService: GestionBesoinsService,
    private auth: AuthService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const besoin: any = JSON.parse(localStorage.getItem('besoin')!) || {
      ordinateurs: [],
      imprimantes: [],
    };
    this.besoin.next(besoin);
    this.besoinState$ = of({
      state: DataState.LOADED,
      data: this.besoin.value,
      success: true,
    });
  }

  public handleSaveBesoins() {
    const besoin = JSON.parse(localStorage.getItem('besoin')!);
    this.besoinState$ = this.gestionBesoinService.addBesoins(besoin).pipe(
      map(() => {
        this.besoin.next({ ordinateurs: [], imprimantes: [] });
        localStorage.removeItem('besoin');
        this.toastService.showSuccessToast(
          EventTypes.Success,
          'Votre demande besoins a été envoyée'
        );
        return { state: DataState.LOADED, sucess: true };
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

  public onCheckboxChange() {
    this.isForDepartement = !this.isForDepartement;
  }

  public handleCloseAddBesoin() {
    this.openAddBesoin = false;
    this.type = '';
  }

  public handleAddRessourceToBesoin(ressourcesForm: NgForm) {
    console.log(ressourcesForm.value);

    this.type === 'Ordinateur'
      ? this.addOrdinateurToLocalStorage(ressourcesForm.value)
      : this.addImprimanteToLocalStorage(ressourcesForm.value);

    this.openAddBesoin = false;
    this.type = '';
  }

  addOrdinateurToLocalStorage(ordinateur: Ordinateur) {
    this.besoin.next({
      ...this.besoin.value,
      ordinateurs: [...this.besoin.value.ordinateurs!, ordinateur],
    });
    localStorage.setItem('besoin', JSON.stringify(this.besoin.value));
  }

  addImprimanteToLocalStorage(imprimante: Imprimante) {
    this.besoin.next({
      ...this.besoin.value,
      imprimantes: [...this.besoin.value.imprimantes!, imprimante],
    });
    localStorage.setItem('besoin', JSON.stringify(this.besoin.value));
  }

  public chooseRessource(typeSelected: any) {
    this.type = typeSelected.target.value;
  }

  public handleRemoveBesoin(type: string, ressource: any) {
    if (type === 'Ordinateur') {
      let id = this.besoin.value.ordinateurs?.indexOf(ressource);
      const ordinateurs: Ordinateur[] = this.besoin.value.ordinateurs?.filter(
        (_, index) => index !== id
      )!;
      this.besoin.next({ ...this.besoin.value, ordinateurs });
    } else {
      let id = this.besoin.value.imprimantes?.indexOf(ressource);
      const imprimantes: Imprimante[] = this.besoin.value.imprimantes?.filter(
        (_, index) => index !== id
      )!;
      this.besoin.next({ ...this.besoin.value, imprimantes });
    }
    localStorage.removeItem('besoin');
    localStorage.setItem('besoin', JSON.stringify(this.besoin.value));
  }

}
