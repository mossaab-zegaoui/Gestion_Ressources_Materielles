import { Component } from '@angular/core';
import { GestionPannesService } from '../../services/gestion-pannes.service';
import { Panne, PanneAction, Ressource } from 'src/app/interface/Classes';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
})
export class MenuComponent {
  Pannes!: Panne[];
  Ressources!: Ressource[];
  changerCount: number | undefined;
  reparerCount: number | undefined;
  panneActions: typeof PanneAction = PanneAction;
  userId: string | null = null;
  roles: string[] | null = null;

  constructor(
    private gestionPannesService: GestionPannesService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.roles = this.auth
      .decodedToken()
      .ROLES.map((role: { authority: any }) => role.authority);
    this.userId = this.auth.decodedToken().userId;
    this.getPannes();
    this.getAllRessources();
    this.updateCounts();
  }

  public hasRole(role: string[]): boolean {
    return this.roles?.some((item) => role.includes(item))!;
  }

  public updateCounts(): void {
    if (this.Pannes && this.Ressources) {
      const changer = this.Pannes.filter(
        (panne) =>
          String(panne.demande) === 'CHANGER' &&
          this.getRessource(panne.idRessource).idFournisseur === this.userId
      );
      this.changerCount = changer.length;

      const reparer = this.Pannes.filter(
        (panne) =>
          String(panne.demande) === 'REPARER' &&
          this.getRessource(panne.idRessource).idFournisseur === this.userId
      );
      this.reparerCount = reparer.length;
    }
  }

  public getRoles(): boolean {
    return true;
  }

  public getPannes(): void {
    this.gestionPannesService.getPanneWithDemandeNotNull().subscribe({
      next: (data: Panne[]) => {
        this.Pannes = data;
        this.updateCounts();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getAllRessources(): void {
    this.gestionPannesService.getAllRessources().subscribe({
      next: (data: Ressource[]) => {
        this.Ressources = data;
        this.updateCounts();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }

  public getRessource(idRessource: number | null): Ressource {
    return this.Ressources.filter(
      (ressource) => ressource.id === idRessource
    )[0];
  }
}
