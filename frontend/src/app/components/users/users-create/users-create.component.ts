import { Component, OnInit } from '@angular/core';
import { UsersService } from '../users.service';
import { Router } from '@angular/router';
import { Users } from '../users.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';


@Component({
  selector: 'app-users-create',
  templateUrl: './users-create.component.html',
  styleUrls: ['./users-create.component.css']
})
export class UsersCreateComponent implements OnInit {

  userForm: FormGroup;

  typeUser: Object[] = [
    {value: 'Student', viewValue: 'Estudante'},
    {value: 'Associate', viewValue: 'Colaborador'}
  ];

  constructor( private usersService: UsersService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({   

      type: ['Student', Validators.required],
      name: ['', Validators.required],
      email: ['', Validators.required],
      active: [true],
      bind: '',
      libraryId: 1,
      courseName: ['', Validators.required],
      department: [''],
      specialty: ['']
    });

    this.userForm.get('type')?.valueChanges.subscribe(value => {
      if (value === 'Student') {
        this.userForm.get('courseName')?.setValidators(Validators.required);
        this.userForm.get('department')?.clearValidators();
        this.userForm.get('specialty')?.clearValidators();
      } else if (value === 'Associate') {
        this.userForm.get('courseName')?.clearValidators();
        this.userForm.get('department')?.setValidators(Validators.required);
        this.userForm.get('specialty')?.setValidators(Validators.required);
      }
      this.userForm.get('courseName')?.updateValueAndValidity();
      this.userForm.get('department')?.updateValueAndValidity();
      this.userForm.get('specialty')?.updateValueAndValidity();
    }); 
  }


  createUsers() {
    this.usersService.create(this.userForm.value).subscribe(() => {
      this.usersService.showMessage('Usuário criado');
      this.router.navigate(['/users']);
    });
  }

  cancel() {
    this.router.navigate(['/users']); // router para voltar à tela anterior
  }

}
