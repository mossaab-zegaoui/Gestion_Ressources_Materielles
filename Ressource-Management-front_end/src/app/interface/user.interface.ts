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
}

export interface Fournisseur extends User {
    addresse?: string;
    nomSociete: string;
    gerant?: string;
    isBlackList?: boolean;
    motifDeBlockage?: string;
}

export interface Technicien extends User {
    specialite: string;
}