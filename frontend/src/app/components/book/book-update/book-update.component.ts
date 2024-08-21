import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../book.model';
import { BookService } from '../book.service';

@Component({
  selector: 'app-book-update',
  templateUrl: './book-update.component.html',
  styleUrls: ['./book-update.component.css']
})
export class BookUpdateComponent implements OnInit {

  book: Book;

  constructor(private bookService : BookService, private router : Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')
 
    if (!(id == null)){
      this.bookService.readById(id).subscribe(book => {
        this.book = book;
      })
    }
  }
  
  updateBook() : void {
    this.bookService.update(this.book).subscribe(() => {
      this.bookService.showMessage("Livro atualizado com sucesso");
      this.router.navigate(["/books"]);
    })
  }

  cancel() : void {
    this.router.navigate(['/books']);
  }
}