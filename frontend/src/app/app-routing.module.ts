import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './view/home/home.component';
import { BookCrudComponent } from './view/book-crud/book-crud.component';
import { BookCreateComponent } from './components/book/book-create/book-create.component';
import { BookDeleteComponent } from './components/book/book-delete/book-delete.component';
import { UsersCrudComponent } from './view/users-crud/users-crud.component';
import { UsersCreateComponent } from './components/users/users-create/users-create.component';
import { UsersDeleteComponent } from './components/users/users-delete/users-delete.component';
import { HttpErrorInterceptor } from './config/HttpErrorInterceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { BookUpdateComponent } from './components/book/book-update/book-update.component';

const routes: Routes = [{
  path: "",
  component: HomeComponent,
}, {
  path: "books",
  component: BookCrudComponent
}, {
  path: "book/create",
  component: BookCreateComponent
}, {
  path: "book/update/:id",
  component: BookUpdateComponent
},
{
  path: "book/delete/:id",
  component: BookDeleteComponent
}, {
  path: "users",
  component: UsersCrudComponent
}, {
  path: "users/create",
  component: UsersCreateComponent
}, 
{
  path: "users/delete/:id",
  component: UsersDeleteComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true }
  ],
})
export class AppRoutingModule { }
