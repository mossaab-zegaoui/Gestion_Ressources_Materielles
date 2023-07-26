import { Injectable } from '@angular/core';
import { Departement, MembreDepartement } from '../interface/Classes';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class GestionDepartementsService {

  private apiServerUrl: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  //departements

  public getAllDepartements(): Observable<Departement[]> {
    return this.http.get<Departement[]>(`${this.apiServerUrl}/departements`);
  }

  public addDepartement(nomDepartement:string | null): Observable<Departement> {
    return this.http.post<Departement>(`${this.apiServerUrl}/departements`, nomDepartement);
  }

  public editDepartement(departement: Departement): Observable<Departement> {
    return this.http.put<Departement>(`${this.apiServerUrl}/departements/${departement.id}`, departement);
  }

  public deleteDepartement(idDepartement: number | null): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/departements/${idDepartement}`)
  }

  //membresDepartement
  public getAllMembers(): Observable<MembreDepartement[]> {
    return this.http.get<MembreDepartement[]>(`${this.apiServerUrl}/membresDepartement`)
  }

  public addMembre(membre: MembreDepartement): Observable<MembreDepartement> {
    return this.http.post<MembreDepartement>(`${this.apiServerUrl}/membresDepartement`, membre);
  }
  public updateMembre(membre: MembreDepartement): Observable<MembreDepartement> {
    return this.http.put<MembreDepartement>(`${this.apiServerUrl}/membresDepartement/${membre.id}`, membre);
  }

  public deleteMembre(idMembre: string  | undefined): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/membresDepartement/${idMembre}`)
  }

  public editMembre(membre: any): Observable<MembreDepartement> {
    return this.http.put<MembreDepartement>(`${this.apiServerUrl}/membresDepartement/${membre.id}`, membre)
  }
}
