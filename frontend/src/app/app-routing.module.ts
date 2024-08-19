import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './view/home/home.component';
import { BookCrudComponent } from './view/book-crud/book-crud.component';
import { BookCreateComponent } from './components/book/book-create/book-create.component';
import { BookDeleteComponent } from './components/book/book-delete/book-delete.component';
import {HttpErrorInterceptor } from './config/HttpErrorInterceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

const routes: Routes = [{
  path: "",
  component: HomeComponent,
}, {
  path: "books",
  component: BookCrudComponent
}, {
  path: "book/create",
  component: BookCreateComponent
}, 
{
  path: "book/delete/:id",
  component: BookDeleteComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true }
  ],
})
export class AppRoutingModule { }
