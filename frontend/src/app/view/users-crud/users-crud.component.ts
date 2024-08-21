import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users-crud',
  templateUrl: './users-crud.component.html',
  styleUrls: ['./users-crud.component.css']
})
export class UsersCrudComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  navigateToUserCreate(): void { // Injeção de dependência para injetar o método (neste caso) dentro do objeto, como um link para navegação
    this.router.navigate(['/users/create']); // Router para navegar para outra página
  }

}
