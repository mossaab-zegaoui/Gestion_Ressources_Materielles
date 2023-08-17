import { Pipe, PipeTransform } from '@angular/core';
import { Departement } from '../interface/Classes';

@Pipe({
  name: 'departementNom',
})
export class DepartementNomPipe implements PipeTransform {
  transform(idDepartement: number, departements: Departement[]): string {
    const departement = departements.find(
      (departement) => departement.id === idDepartement
    )!;
    return departement?.nomDepartement;
  }
}
