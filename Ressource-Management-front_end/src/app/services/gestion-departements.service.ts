import { Injectable } from '@angular/core';
import { Departement, MembreDepartement } from '../interface/Classes';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class GestionDepartementsService {
  private apiServerUrl: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  //departements

  public getAllDepartements(): Observable<Departement[]> {
    return this.http.get<Departement[]>(`${this.apiServerUrl}/departements`);
  }

  public addDepartement(
    nomDepartement: string | null
  ): Observable<Departement> {
    return this.http.post<Departement>(
      `${this.apiServerUrl}/departements`,
      nomDepartement
    );
  }

  public editDepartement(departement: Departement): Observable<Departement> {
    return this.http.put<Departement>(
      `${this.apiServerUrl}/departements/${departement.id}`,
      departement
    );
  }

  public deleteDepartement(idDepartement: number | null): any {
    return this.http.delete(
      `${this.apiServerUrl}/departements/${idDepartement}`
    );
  }

  //membresDepartement
  public getAllMembers(): Observable<MembreDepartement[]> {
    return this.http
      .get<MembreDepartement[]>(`${this.apiServerUrl}/membresDepartement`)
      .pipe(tap(console.log), catchError(this.handleError));
  }

  public addMembre(membre: MembreDepartement): Observable<MembreDepartement> {
    return this.http.post<MembreDepartement>(
      `${this.apiServerUrl}/membresDepartement`,
      membre
    );
  }
  public updateMembre(
    membre: MembreDepartement
  ): Observable<MembreDepartement> {
    return this.http
      .put<MembreDepartement>(
        `${this.apiServerUrl}/membresDepartement/${membre.id}`,
        membre
      )
      .pipe(tap(console.log), catchError(this.handleError));
  }

  public deleteMembre(idMembre: string | undefined): Observable<void> {
    return this.http.delete<void>(
      `${this.apiServerUrl}/membresDepartement/${idMembre}`
    );
  }

  public editMembre(membre: any): Observable<MembreDepartement> {
    return this.http.put<MembreDepartement>(
      `${this.apiServerUrl}/membresDepartement/${membre.id}`,
      membre
    );
  }
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    let errorMessage: string;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client error occurred - ${error.error.message}`;
    } else {
      if (error.error.message) {
        errorMessage = error.error.message;
        console.log(errorMessage);
      } else {
        errorMessage = `An error occurred - Error status ${error.status}`;
      }
    }
    return throwError(() => errorMessage);
  }
}
