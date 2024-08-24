import { Component, OnInit } from '@angular/core';
import { Loan } from '../loan.model';
import { LoanService } from '../loan.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-loan-read',
  templateUrl: './loan-read.component.html',
  styleUrls: ['./loan-read.component.css']
})
export class LoanReadComponent implements OnInit {

  loan: Loan [] = [];
  // todo: colocar em ordem essa tabela 
  displayedColumns = ['bookId', 'userId', 'title', 'name', 'effectiveFrom', 'effectiveTo', 'delivered', 'author', 'email', 'action'];


  constructor(private loanService: LoanService, private formBuilder: FormBuilder) { }



  ngOnInit(): void {
    this.loanService.read().subscribe(loan => {
      this.loan = loan;
      console.log(loan);
    })
  }

  isOverdue(loan: Loan) {
    const today = new Date();
    const dueDate = new Date(loan.effectiveTo);
    return !loan.delivered && dueDate < today;
  }
}
