import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Users } from '../users.model';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-users-update',
  templateUrl: './users-update.component.html',
  styleUrls: ['./users-update.component.css']
})
export class UsersUpdateComponent implements OnInit {

  users: Users;
  typeUser: Object[] = [
    {value: 'Student', viewValue: 'Estudante'},
    {value: 'Associate', viewValue: 'Colaborador'}
  ];
  
  constructor(private usersService : UsersService, private router : Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')
 
    if (!(id == null)){
      this.usersService.readById(id).subscribe(users => {
        this.users = users;
      })
    }
  }
  
  updateUsers() : void {
    this.usersService.update(this.users).subscribe(() => {
      this.usersService.showMessage("Livro atualizado com sucesso");
      this.router.navigate(["/users"]);
    })
  }

  cancel() : void {
    this.router.navigate(['/users']);
  }
}