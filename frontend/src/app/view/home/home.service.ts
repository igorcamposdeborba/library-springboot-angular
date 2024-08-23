import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable } from 'rxjs';
import { Quote } from './home.model';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  private baseUrl: string="http://localhost:8080/";

  constructor(private snackBar: MatSnackBar, private http: HttpClient) {  } // injeção de dependência para usar/modificar recursos externos na minha página

  showMessage(msg: string, isError: boolean = false) : void {
    this.snackBar.open(msg, 'X', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: isError? ['msg-error'] : ['msg-success']
    })
  }

  read () : Observable<Quote> {
    return this.http.get<Quote>(this.baseUrl + "random-quote").pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }
  
  errorHandler(error: any) : Observable<any> {
    this.showMessage(`Erro: ${error.message}`, true);
    return EMPTY;
  }
}