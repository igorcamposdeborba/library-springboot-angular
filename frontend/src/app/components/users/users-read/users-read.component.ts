import { Component, OnInit } from '@angular/core';
import { Users } from '../users.model';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-users-read',
  templateUrl: './users-read.component.html',
  styleUrls: ['./users-read.component.css']
})
export class UsersReadComponent implements OnInit {

  users: Users[] = [];
  displayedColumns = ['id', 'type', 'name', 'email', 'active', 'bind', 'libraryId', 'action'];


  constructor(private usersService: UsersService) { }

  ngOnInit(): void {
    this.usersService.read().subscribe(users => {
      this.users = users;
      console.log(users);
    })
  }
}
