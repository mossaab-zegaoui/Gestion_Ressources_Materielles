import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient } from '@angular/common/http';
import { AppelOffre, NotifFournisseur, Offre } from '../interface/Classes';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OffreService {

  private apiServerUrl: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }


  public getOffreByAppelOffre(appelOffre:AppelOffre|null):Observable<Offre[]>{
    return this.http.get<Offre[]>(`${this.apiServerUrl}/offre/AppelOffre/${appelOffre!.id}`);
  }

  public getAllOffres():Observable<Offre[]>{
    return this.http.get<Offre[]>(`${this.apiServerUrl}/offre`);
  }

 public getOffresFournisseur(id:string | null):Observable<Offre[]>{

  return this.http.get<Offre[]>(`${this.apiServerUrl}/offre/Fournisseur/${id}`);
}

  public saveOffre(offre:Offre|null):Observable<Offre>{

    return this.http.post<Offre>(`${this.apiServerUrl}/offre`,offre);
 }

 public accepterOffre(offre:Offre|undefined):Observable<void>{

    return this.http.put<void>(`${this.apiServerUrl}/offre/accepter/${offre?.id}`,offre);
 }

 public getNotifFournisseur(idFournisseur: string): Observable<NotifFournisseur[]> {

    return this.http.get<NotifFournisseur[]>(`${this.apiServerUrl}/notifFournisseur/fournisseur/${idFournisseur}`)
 }

 public notifSeen(id: number | null):Observable<void> {

    return this.http.put<void>(`${this.apiServerUrl}/notifFournisseur/${id}`, null)
 }


}
