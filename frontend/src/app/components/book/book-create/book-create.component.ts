import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Router } from '@angular/router';
import { Book } from '../book.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-book-create',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.css']
})
export class BookCreateComponent implements OnInit {
  bookForm: FormGroup;

  book: Book = {
    title: '',
    author: '',
    yearPublication: '',
    price: 4.00,
    libraryId: 1
  }

  constructor( private bookService: BookService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.bookForm = this.formBuilder.group({   

      title: ['', Validators.required],
      author: ['', Validators.required],
      yearPublication: ['', Validators.required],
      price: [4],
      libraryId: 1
    });

    this.bookForm.valueChanges.subscribe(value => {
      this.bookForm.get('title')?.setValidators(Validators.required);      
      this.bookForm.get('author')?.setValidators(Validators.required);
      this.bookForm.get('yearPublication')?.setValidators(Validators.required);
      this.bookForm.get('price')?.clearValidators();
    }); 
  }

  createBook() {
    this.bookService.create(this.bookForm.value).subscribe(() => {
      this.bookService.showMessage('Livro criado');
      this.router.navigate(['/books']);
    });
  }

  cancel() {
    this.router.navigate(['/books']); // router para voltar à tela anterior
  }

}
