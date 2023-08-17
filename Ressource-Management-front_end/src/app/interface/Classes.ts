export interface Departement {
  id?: number | null;
  nomDepartement: string;
  membreDepartements?: MembreDepartement[] | null;
}

export interface MembreDepartement {
  id?: string;
  username: string;
  password: string;
  nom: string;
  prenom: string;
  cin?: string;
  email: string;
  domaineExpertise?: string;
  laboratoire?: string;
  departement: Departement;
  roles: Role[];
}

export interface Role {
  id: number | null;
  nomRole: string;
}

export interface Ressource {
  id: number | null;
  codeBarre: string | null;
  dateLivraison: Date | null;
  dateFinGarantie: Date | null;
  marque: string | null;
  prix: number | null;
  type: string | null;
  idFournisseur: string | null;
  idMembreDepartement: string | null;
  idDepartement: number | null;
  isDeleted: boolean | false;
}

export interface Ordinateur extends Ressource {
  disquedur: any;
  cpu: string;
  ram: number;
  disqueDur: string;
  ecran: string;
}

export interface Imprimante extends Ressource {
  resolution: string;
  vitesseImpression: string;
}

export interface Besoin {
  id?: number | null;
  dateDemande?: string;
  dateAffectation?: string | null;
  isAffected?: boolean;
  idMembreDepartement?: string;
  idDepartement?: number;
  nomDepartement?:string;
  isBesoinInAppelOffre?: boolean;
  ordinateurs?: Ordinateur[];
  imprimantes?: Imprimante[];
  isSelected?: boolean;
}

export interface Demande {
  id: number;
  message: string;
  isSeen: boolean;
  dateDemande: Date;
  idMembreDepartement: string;
  idDepartement: number;
}

export interface AppelOffre {
  id: number | null;
  datePub: string | null | Date;
  isAffected: boolean;
  besoins: Besoin[];
}

export interface Offre {
  id: number | null;
  dateDebut: string | null;
  dateFin: string | null;
  isAffected: boolean;
  isRejected: boolean;
  isWaiting: boolean;
  idFournisseur: string | null;
  ressources: RessourceFournisseur[];
  idAppelOffre: number;
}

export interface RessourceFournisseur {
  id: number | null;
  marque: string | null;
  prix: number | null;
  idRessource: number | null;
}

export interface fournisseur {
  id: string;
  username: string;
  password: string;
  nom: string;
  prenom: string;
  cin: string;
  email: string;
  addresse: string;
  gerant: string;
  nomSociete: string;
  isBlackList: string;
  motifDeBlockage: string;
}

export enum PanneFrequence {
  RARE,
  FREQUENTE,
  PERMANENTE,
}

export enum OrdrePanne {
  DEFAULT_SYSTEME,
  DEFAULT_LOGICIEL_UTILITAIRE,
  DEFAULT_MATERIEL,
}

export enum PanneAction {
  REPARER,
  CHANGER,
}

export interface Panne {
  id: number | null;
  explication: String | null;
  dateApparition: Date | null;
  constat: string | null;
  dateConstat: Date | null;
  ordre: OrdrePanne | null;
  frequence: PanneFrequence | null;
  isTreated: Boolean | null;
  idMembreDepartement: string | null;
  idTechnicien: string | null;
  idRessource: number | null;
  demande: PanneAction | null;
}
export interface RessourceEnPanne {

  idPanne: number;
  codeBarre:string;
  idRessource: number;
  idMembreDepartement:string;
  dateApparition: Date;
  idTechnicien?: string;
  marque: string;
  type: string;
}

export interface Technicien {
  id: string | null;
  cin: string | null;
  email: string | null;
  nom: string | null;
  prenom: string | null;
  username: string | null;
  password: string | null;
  specialite: string | null;
}

export interface NotifFournisseur {
  id: number | null;
  isSeen: boolean | null;
  idFournisseur: string | null;
  dateOffre: Date | null;
  isAccepted: boolean | null;
}
