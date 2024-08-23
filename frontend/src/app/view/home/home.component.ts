import { Component, OnInit } from '@angular/core';
import { Quote } from './home.model';
import { HomeService } from './home.service';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  quote : Quote;

  displayedColumns = ['reference', 'text'];

  constructor(private quoteService: HomeService) { }

  ngOnInit(): void {
    this.quoteService.read().subscribe(quote => {
      this.quote = quote;
    })
  }


}
