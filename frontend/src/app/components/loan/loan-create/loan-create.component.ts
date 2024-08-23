import { Component, OnInit } from '@angular/core';
import { LoanService } from '../loan.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Loan } from '../loan.model';

@Component({
  selector: 'app-loan-create',
  templateUrl: './loan-create.component.html',
  styleUrls: ['./loan-create.component.css']
})
export class LoanCreateComponent implements OnInit {
  loanForm: FormGroup;

  constructor( private loanService: LoanService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.loanForm = this.formBuilder.group({   

      title: ['', Validators.required],
      author: ['', Validators.required],
      yearPublication: ['', Validators.required],
      price: [4],
      libraryId: 1
    });

    this.loanForm.valueChanges.subscribe(value => {
      this.loanForm.get('title')?.setValidators(Validators.required);      
      this.loanForm.get('author')?.setValidators(Validators.required);
      this.loanForm.get('yearPublication')?.setValidators(Validators.required);
      this.loanForm.get('price')?.clearValidators();
    }); 
  }

  createLoan() {
    this.loanService.create(this.loanForm.value).subscribe(() => {
      this.loanService.showMessage('Empréstimo criado');
      this.router.navigate(['/loan']);
    });
  }

  cancel() {
    this.router.navigate(['/loan']); // router para voltar à tela anterior
  }

}
