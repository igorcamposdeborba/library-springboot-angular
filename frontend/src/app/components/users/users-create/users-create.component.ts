import { Component, OnInit } from '@angular/core';
import { UsersService } from '../users.service';
import { Router } from '@angular/router';
import { Users } from '../users.model';

@Component({
  selector: 'app-users-create',
  templateUrl: './users-create.component.html',
  styleUrls: ['./users-create.component.css']
})
export class UsersCreateComponent implements OnInit {

  users: Users = {
    type: '',
    name: '',
    email: '',
    active: true,
    bind: '',
    libraryId: 1
  }
  userTypes = ['Associate', 'Student'];
  constructor( private usersService: UsersService, private router: Router) { }

  ngOnInit(): void {
    
  }

  createUsers() {
    this.usersService.create(this.users).subscribe(() => {
      this.usersService.showMessage('Livro criado');
      this.router.navigate(['/users']);
    });
  }

  cancel() {
    this.router.navigate(['/users']); // router para voltar à tela anterior
  }

}
