import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-book-crud',
  templateUrl: './book-crud.component.html',
  styleUrls: ['./book-crud.component.css']
})
export class BookCrudComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  navigateToBookCreate(): void { // Injeção de dependência para injetar o método (neste caso) dentro do objeto, como um link para navegação
    this.router.navigate(['/book/create']); // Router para navegar para outra página
  }

}
