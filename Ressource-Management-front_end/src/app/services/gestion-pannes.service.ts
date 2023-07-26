import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { Observable } from 'rxjs';
import {
  Panne,
  Ressource,
  Technicien,
  RessourceEnPanne,
} from '../interface/Classes';

@Injectable({
  providedIn: 'root',
})
export class GestionPannesService {
  private apiServerUrl: string = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  public getAllPannes(): Observable<Panne[]> {
    return this.http.get<Panne[]>(`${this.apiServerUrl}/pannes`);
  }

  public getAllRessources(): Observable<Ressource[]> {
    return this.http.get<Ressource[]>(`${this.apiServerUrl}/ressources`);
  }

  public getMemberDepartementRessources(
  idMember: string
  ): Observable<Ressource[]> {
    return this.http.get<Ressource[]>(
      `${this.apiServerUrl}/ressources/membreDepartement/${idMember}`
    );
  }

  public getMemberDepartementPannes(idMember: string): Observable<Panne[]> {
    console.warn(`${this.apiServerUrl}/pannes/membreDepartement/${idMember}`);
    return this.http.get<Panne[]>(
      `${this.apiServerUrl}/pannes/membreDepartement/${idMember}`

    );
  }

  public getPannesNotTreated(): Observable<Panne[]> {
    return this.http.get<Panne[]>(`${this.apiServerUrl}/pannes/pannesNotTreated`);
  }

  public getPannesAvecRessources():Observable<RessourceEnPanne[]>{
    return this.http.get<RessourceEnPanne[]>(`${this.apiServerUrl}/pannes/pannesAvecRessources`);

  }
  public getPanneWithConstatNotNull(): Observable<Panne[]> {
    return this.http.get<Panne[]>(`${this.apiServerUrl}/pannes/constats`);
  }

  public getPanneWithDemandeNotNull(): Observable<Panne[]> {
    return this.http.get<Panne[]>(`${this.apiServerUrl}/pannes/demandes`);
  }

  public addPanne(panne: Panne): Observable<Panne> {
    return this.http.post<Panne>(`${this.apiServerUrl}/pannes`, panne);
  }

  public editPanne(panne: Panne): Observable<Panne> {
    let idPanne = panne.id;
    return this.http.put<Panne>(
      `${this.apiServerUrl}/pannes/${idPanne}`,
      panne
    );
  }

  public deletePanne(idPanne: number | null): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/panness/${idPanne}`);
  }

  public getAllTechniciens(): Observable<Technicien[]> {
    return this.http.get<Technicien[]>(`${this.apiServerUrl}/technicien`);
  }

  public addTechnicien(technicien: Technicien): Observable<Technicien> {
    return this.http.post<Technicien>(
      `${this.apiServerUrl}/technicien`,
      technicien
    );
  }

  public deleteTechnicien(idTechnicien: string | null): Observable<void> {
    return this.http.delete<void>(
      `${this.apiServerUrl}/technicien/${idTechnicien}`
    );
  }

  public editTechnicien(technicien: Technicien): Observable<Technicien> {
    return this.http.put<Technicien>(
      `${this.apiServerUrl}/technicien/${technicien.id}`,
      technicien
    );
  }
}
