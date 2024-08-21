export interface Users {
    id?: number; // ? significa que o atributo é opcional
    type: string;
    name: string;
    email: string;
    active: boolean;
    bind: string;
    libraryId: number;
}