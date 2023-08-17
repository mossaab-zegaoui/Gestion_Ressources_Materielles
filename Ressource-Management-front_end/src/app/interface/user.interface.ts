import { Role } from "./Classes";

export interface User {
    username: string;
    password: string;
    email?: string;
    id?: string;
    cin?: string;
    nom?: string;
    prenom?: string;
    roles?: Role[];
    enabled:boolean;
}

export interface Fournisseur extends User {
    addresse?: string;
    nomSociete: string;
    gerant?: string;
    blackList?: boolean;
    motifDeBlockage?: string;
}

export interface Technicien extends User {
    specialite: string;
}