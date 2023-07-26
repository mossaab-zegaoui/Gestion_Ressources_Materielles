import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {fournisseur, Imprimante, MembreDepartement, Ordinateur} from "../interface/Classes";

@Injectable({
  providedIn: 'root'
})
export class ImprimanteService {

  private apiServerUrl: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getImprimanteDisponible(): Observable<Imprimante[]> {
    return this.http.get<Imprimante[]>(`${this.apiServerUrl}/imprimantes/listeDisponible`);
  }

  public getFournisseur(): Observable<fournisseur[]> {
    return this.http.get<fournisseur[]>(`${this.apiServerUrl}/fournisseurs/listeFournisseur`);
  }

  public getEnseignant(): Observable<MembreDepartement[]> {
    return this.http.get<MembreDepartement[]>(`${this.apiServerUrl}/membresDepartement`);
  }


  public getImprimanteNonLivre(): Observable<Imprimante[]> {
    return this.http.get<Imprimante[]>(`${this.apiServerUrl}/imprimantes/listeLivree`);
  }

  public deleteImprimante(id :number | null) : Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/imprimantes/${id}`)
  }

  public modifierImprimante(imprimante: Imprimante | undefined) : Observable<Imprimante>{
    return this.http.put<Imprimante>(`${this.apiServerUrl}/imprimantes/${imprimante?.id}`, imprimante);
  }
  public modifierFournisseur(fournisseur: fournisseur | undefined) : Observable<fournisseur>{
    return this.http.put<fournisseur>(`${this.apiServerUrl}/fournisseurs/${fournisseur?.id}`, fournisseur);
  }








}
