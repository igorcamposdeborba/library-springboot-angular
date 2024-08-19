import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Router } from '@angular/router';
import { Book } from '../book.model';

@Component({
  selector: 'app-book-create',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.css']
})
export class BookCreateComponent implements OnInit {

  book: Book = {
    title: '',
    author: '',
    yearPublication: '',
    price: 4.00,
    libraryId: 1
  }

  constructor( private bookService: BookService, private router: Router) { }

  ngOnInit(): void {
  }

  createBook() {
    this.bookService.create(this.book).subscribe(() => {
      this.bookService.showMessage('Livro criado');
      this.router.navigate(['/books']);
    });
  }

  cancel() {
    this.router.navigate(['/books']); // router para voltar à tela anterior
  }

}
