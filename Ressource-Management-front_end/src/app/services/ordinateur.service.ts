import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Departement, fournisseur, MembreDepartement, Ordinateur} from "../interface/Classes";

@Injectable({
  providedIn: 'root'
})
export class OrdinateurService {
  private apiServerUrl: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getOrdinateurDisponible(): Observable<Ordinateur[]> {
    return this.http.get<Ordinateur[]>(`${this.apiServerUrl}/ordinateurs/listeDisponible`);
  }

  public getFournisseur(): Observable<fournisseur[]> {
    return this.http.get<fournisseur[]>(`${this.apiServerUrl}/fournisseurs/listeFournisseur`);
  }

  public getEnseignant(): Observable<MembreDepartement[]> {
    return this.http.get<MembreDepartement[]>(`${this.apiServerUrl}/membresDepartement`);
  }


  public getOrdinateurNonLivre(): Observable<Ordinateur[]> {
    return this.http.get<Ordinateur[]>(`${this.apiServerUrl}/ordinateurs/listeLivree`);
  }

  public deleteOrdinateur(id :number | null) : Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/ordinateurs/${id}`)
  }

 public modifierOrdinateur(ordinateur: Ordinateur | undefined) : Observable<Ordinateur>{
   return this.http.put<Ordinateur>(`${this.apiServerUrl}/ordinateurs/${ordinateur?.id}`, ordinateur);
 }
  public modifierFournisseur(fournisseur: fournisseur | undefined) : Observable<fournisseur>{
    return this.http.put<fournisseur>(`${this.apiServerUrl}/fournisseurs/${fournisseur?.id}`, fournisseur);
  }




}
