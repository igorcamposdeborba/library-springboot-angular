import { Component, OnInit } from '@angular/core';
import { Loan } from '../loan.model';
import { LoanService } from '../loan.service';

@Component({
  selector: 'app-loan-read',
  templateUrl: './loan-read.component.html',
  styleUrls: ['./loan-read.component.css']
})
export class LoanReadComponent implements OnInit {

  loans: Loan[] = [];
  // todo: colocar em ordem essa tabela
  displayedColumns = ['effectiveFrom', 'effectiveTo', 'bookId', 'title', 'author', 'userId', 'name', 'email', 'delivered', 'libraryId', 'action'];


  constructor(private loanService: LoanService) { }

  ngOnInit(): void {
    this.loanService.read().subscribe(loan => {
      this.loans = loan;
      console.log(loan);
    })
  }
}
