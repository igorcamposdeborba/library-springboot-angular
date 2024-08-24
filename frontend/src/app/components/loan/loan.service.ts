import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable } from 'rxjs';
import { Loan } from './loan.model';
import { LoanCreate } from './loan-create.model';

@Injectable({
  providedIn: 'root'
})
export class LoanService {

  private baseUrl: string="http://localhost:8080/loan";

  constructor(private snackBar: MatSnackBar, private http: HttpClient) {  }

  showMessage(msg: string, isError: boolean = false) : void {
    this.snackBar.open(msg, 'X', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: isError? ['msg-error'] : ['msg-success']
    })
  }

  create(loan: LoanCreate): Observable<Loan>{ // comunicação com o banco de dados

    return this.http.post<Loan>(this.baseUrl, loan).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  read () : Observable<Loan[]> {
    return this.http.get<Loan[]>(this.baseUrl).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  readById(bookId: number, userId: number): Observable<Loan> {
    const url = `${this.baseUrl}/find`;
    const body = { bookId, userId };
    return this.http.post<Loan>(url, body).pipe(
      map(obj => obj),
      catchError(e => this.errorHandler(e))
    );
  }

  update (loanData: Loan): Observable<Loan> {
    return this.http.post<Loan>(this.baseUrl + '/deliver', loanData);
  }

  errorHandler(error: any) : Observable<any> {
    this.showMessage(`Erro: ${error.message}`, true);
    return EMPTY;
  }
}