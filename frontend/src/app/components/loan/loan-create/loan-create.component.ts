import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { LoanService } from '../loan.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Loan } from '../loan.model';
import { BookService } from '../../book/book.service';
import { Book } from '../../book/book.model';
import { Users } from '../../users/users.model';
import { UsersService } from '../../users/users.service';
import { LoanCreate } from '../loan-create.model';

@Component({
  selector: 'app-loan-create',
  templateUrl: './loan-create.component.html',
  styleUrls: ['./loan-create.component.css']
})
export class LoanCreateComponent implements OnInit {
  loanForm: FormGroup;
  bookForm: FormGroup;

  effectiveFromActual : string;
  effectiveToActual : string;

  bookIdSelected : number;
  books: Book [] = [];
  user : Users;
  loan: LoanCreate = {
    bookId: 0,
    userId: 0,
    effectiveFrom: '',
    effectiveTo: ''
  };

  constructor(private bookService: BookService, private loanService : LoanService, private usersService: UsersService, private router: Router, private route: ActivatedRoute,
              private formBuilder: FormBuilder, private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.bookService.read().subscribe(book => {
      this.books = book;
      this.changeDetector.markForCheck(); // Forçar detecção de alterações
      console.log(book);
    })

    this.loanForm = this.formBuilder.group({   
      bookId: [],
      userId: [],
      effectiveFrom: [''],
      effectiveTo: ['']
    });

    this.loanForm.get('bookId')?.valueChanges.subscribe(value => {
      this.loanForm.get('effectiveFrom')?.updateValueAndValidity();
      this.loanForm.get('effectiveTo')?.updateValueAndValidity();
    }); 

    this.bookForm = this.formBuilder.group({   
      id: [],
      title: [''],
      author: [''],
      yearPublication: [''],
      price: [],
      libraryId: []
    });


    this.bookForm.get('id')?.valueChanges.subscribe(value => {
      this.bookForm.get('title')?.updateValueAndValidity();
      this.bookForm.get('author')?.updateValueAndValidity();
      this.bookForm.get('yearPublication')?.updateValueAndValidity();
      this.bookForm.get('price')?.updateValueAndValidity();
      this.bookForm.get('libraryId')?.updateValueAndValidity();
    }); 
  }

  createLoan() {
    this.loan.bookId = this.bookIdSelected;
    this.loan.userId = this.user.id;
    this.loan.effectiveFrom = this.effectiveFromActual;
    this.loan.effectiveTo = this.effectiveToActual;
    this.loanService.create(this.loan).subscribe(() => {
      this.loanService.showMessage('Empréstimo criado');
      this.router.navigate(['/loan']);
    });
  }

  findUserByEmail(emailInput: any){
    console.log(emailInput.target.value);
    this.usersService.readByEmail(emailInput.target.value).subscribe(user => {
        this.user = user;
        this.loan.userId = user.id;
      });
  }

  onSelectionChange(event: any) {
    console.log("onSelectionChange " + event)
    this.bookIdSelected = event;
    this.changeDetector.markForCheck();
  }

  cancel() {
    this.router.navigate(['/loan']); // router para voltar à tela anterior
  }

}
