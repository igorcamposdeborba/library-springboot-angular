export interface Book {
    id?: number; // ? significa que o atributo é opcional
    title: string;
    author: string;
    yearPublication: String;
    price: number;
    libraryId: number;
}