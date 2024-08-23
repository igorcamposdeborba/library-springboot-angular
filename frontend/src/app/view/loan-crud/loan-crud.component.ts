import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-loan-crud',
  templateUrl: './loan-crud.component.html',
  styleUrls: ['./loan-crud.component.css']
})
export class LoanCrudComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  navigateToLoanCreate(): void { // Injeção de dependência para injetar o método (neste caso) dentro do objeto, como um link para navegação
    this.router.navigate(['/loan/create']); // Router para navegar para outra página
  }

}
