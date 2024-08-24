import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Loan } from '../loan.model';
import { LoanService } from '../loan.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-loan-update',
  templateUrl: './loan-update.component.html',
  styleUrls: ['./loan-update.component.css']
})
export   
 class LoanUpdateComponent implements OnInit   
 {

  loan: Loan | undefined;

  constructor(private loanService: LoanService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const bookId = params['bookId'];
      const userId = params['userId'];

      if (bookId && userId) {
        this.loanService.readById(bookId, userId).subscribe(
          loan => {
            this.loan = loan;
          },
          error => {
            console.error("Error fetching loan:", error);
          }
        );
      } else {
        console.error("Missing bookId or userId in query parameters");
      }
    });
  }

  updateLoan(): void {
    if (this.loan) { // Check if loan is available before update
      this.loanService.update(this.loan).subscribe(() => {
        this.loanService.showMessage("Empréstimo atualizado com sucesso");
        this.router.navigate(["/loan"]);
      });
    } else {
      console.error("Loan data not available for update");
    }
  }

  cancel(): void {
    this.router.navigate(['/loan']);
  }
}