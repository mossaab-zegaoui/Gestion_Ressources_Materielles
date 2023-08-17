import { Injectable } from '@angular/core';
import { Besoin, Demande, Imprimante, Ordinateur } from '../interface/Classes';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class GestionBesoinsService {

  private apiServerUrl: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public addBesoins(besoin: Besoin): Observable<Besoin> {
    return this.http.post<Besoin>(`${this.apiServerUrl}/besoins`, besoin);
  }

  public getBesoinsByIdDepartement(): Observable<Besoin[]> {
    return this.http.get<Besoin[]>(`${this.apiServerUrl}/besoins/departement`);
  }

  public getBesoinsNotInAppelOffre(): Observable<Besoin[]> {
    return this.http.get<Besoin[]>(`${this.apiServerUrl}/besoins/NotInAppelOffre`);
  }

  public sendBesoinsRequest(): Observable<void> {
    return this.http.post<void>(`${this.apiServerUrl}/demandes/departements`, null);
  }

  public getDemandesByIdMembre(idMembre: string): Observable<Demande[]> {
    return this.http.get<Demande[]>(`${this.apiServerUrl}/demandes/membres/${idMembre}`);
  }

  public demandeSeen(id: number): Observable<Demande> {
    return this.http.put<Demande>(`${this.apiServerUrl}/demandes/seen/${id}`, null);
  }

}
