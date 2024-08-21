import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable } from 'rxjs';
import { Users } from './users.model';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private baseUrl: string="http://localhost:8080/user";

  constructor(private snackBar: MatSnackBar, private http: HttpClient) {  } // injeção de dependência para usar/modificar recursos externos na minha página

  showMessage(msg: string, isError: boolean = false) : void {
    this.snackBar.open(msg, 'X', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: isError? ['msg-error'] : ['msg-success']
    })
  }

  create(users: Users): Observable<Users>{ // comunicação com o banco de dados

    return this.http.post<Users>(this.baseUrl + '/single', users).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  read () : Observable<Users[]> {
    return this.http.get<Users[]>(this.baseUrl).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  readById(id : number) : Observable<Users> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Users>(url).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  update (users: Users) : Observable<Users> {
    const url = `${this.baseUrl}/${users.id}`;
    return this.http.put<Users>(url, users).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  delete (id : number) : Observable<Users> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete<Users>(url).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }
  
  errorHandler(error: any) : Observable<any> {
    this.showMessage(`Erro: ${error.message}`, true);
    return EMPTY;
  }
}