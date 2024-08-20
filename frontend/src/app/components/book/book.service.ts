import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable } from 'rxjs';
import { Book } from './book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private baseUrl: string="http://localhost:8080/book";

  constructor(private snackBar: MatSnackBar, private http: HttpClient) {  } // injeção de dependência para usar/modificar recursos externos na minha página

  showMessage(msg: string, isError: boolean = false) : void {
    this.snackBar.open(msg, 'X', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: isError? ['msg-error'] : ['msg-success']
    })
  }

  create(book: Book): Observable<Book>{ // comunicação com o banco de dados

    return this.http.post<Book>(this.baseUrl + '/single', book).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  read () : Observable<Book[]> {
    return this.http.get<Book[]>(this.baseUrl).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  readById(id : number) : Observable<Book> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Book>(url).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  update (book: Book) : Observable<Book> {
    const url = `${this.baseUrl}/${book.id}`;
    return this.http.put<Book>(url, book).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  delete (id : number) : Observable<Book> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete<Book>(url).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }
  
  errorHandler(error: any) : Observable<any> {
    this.showMessage(`Erro: ${error.message}`, true);
    return EMPTY;
  }
}