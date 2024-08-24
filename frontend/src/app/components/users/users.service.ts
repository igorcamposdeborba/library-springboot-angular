import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable } from 'rxjs';
import { Users } from './users.model';
import { UserEmail } from './userEmail';

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

  readByEmail(userEmail: UserEmail): Observable<Users> {
    const url = `${this.baseUrl}/email`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' }); // No Content-Type header needed
  
    return this.http.post<UserEmail>(url, new String(userEmail), {headers})
      .pipe(
        map(user => user),
        catchError(e => this.errorHandler(e))
      );
  }

  update (user: Users) : Observable<Users> {
    const url = `${this.baseUrl}/${user.id}`;
    return this.http.put<Users>(url, user).pipe(
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