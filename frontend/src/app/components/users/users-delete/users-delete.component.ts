import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Users } from '../users.model';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-users-delete',
  templateUrl: './users-delete.component.html',
  styleUrls: ['./users-delete.component.css']
})
export class UsersDeleteComponent implements OnInit {

  users: Users;


  constructor(private usersService: UsersService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.usersService.readById(id).subscribe(users => {
      this.users = users;
    })
  }

  deleteusers () : void {
    this.usersService.delete(this.users.id).subscribe(() => {
      this.usersService.showMessage("Usuário excluído com sucesso");
      this.router.navigate(["/users"]);
    })
  }

  cancel() : void {
    this.router.navigate(['/users']);
  }
}
