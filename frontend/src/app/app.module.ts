import { NgModule, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './components/template/header/header.component';
import { MatToolbarModule }  from '@angular/material/toolbar';
import { FooterComponent } from './components/template/footer/footer.component';
import { NavComponent } from './components/template/nav/nav.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { HomeComponent } from './view/home/home.component';
import { BookCrudComponent } from './view/book-crud/book-crud.component';
import { UsersCrudComponent } from './view/users-crud/users-crud.component';
import { RedDirective } from './directives/red.directive';
import { ForDirective } from './directives/for.directive';
import { BookCreateComponent } from './components/book/book-create/book-create.component';
import { UsersCreateComponent } from './components/users/users-create/users-create.component';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BookReadComponent } from './components/book/book-read/book-read.component';
import { UsersReadComponent } from './components/users/users-read/users-read.component';
import { MatTableModule } from '@angular/material/table'
import localePt from '@angular/common/locales/pt';
import { registerLocaleData } from '@angular/common';
import { BookUpdateComponent } from './components/book/book-update/book-update.component';
import { BookDeleteComponent } from './components/book/book-delete/book-delete.component';
import { UsersDeleteComponent } from './components/users/users-delete/users-delete.component';
import { UsersUpdateComponent } from './components/users/users-update/users-update.component';
import { MatOptionModule } from '@angular/material/core';
import { LoanCreateComponent } from './components/loan/loan-create/loan-create.component';
import { LoanReadComponent } from './components/loan/loan-read/loan-read.component';
import { LoanCrudComponent } from './view/loan-crud/loan-crud.component';
import { LoanUpdateComponent } from './components/loan/loan-update/loan-update.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';


registerLocaleData (localePt);

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    NavComponent,
    HomeComponent,
    BookCrudComponent,
    UsersCrudComponent,
    RedDirective,
    ForDirective,
    BookCreateComponent,
    BookReadComponent,
    BookUpdateComponent,
    BookDeleteComponent,
    UsersCreateComponent,
    UsersReadComponent,
    UsersDeleteComponent,
    UsersUpdateComponent,
    LoanReadComponent,
    LoanCreateComponent,
    LoanUpdateComponent,
    LoanCrudComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatSnackBarModule,
    HttpClientModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatOptionModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    MatSelectModule
  ],
  providers: [{
    provide: LOCALE_ID,
    useValue: 'pt-br'
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
