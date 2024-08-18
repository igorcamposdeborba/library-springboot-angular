import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';
import { Product } from '../product.model';

@Component({
  selector: 'app-product-create',
  templateUrl: './product-create.component.html',
  styleUrls: ['./product-create.component.css']
})
export class ProductCreateComponent implements OnInit {

  product: Product = {
    title: '',
    author: '',
    yearPublication: '',
    price: 4.00,
    libraryId: 1
  }

  constructor( private productService: ProductService, private router: Router) { }

  ngOnInit(): void {
  }

  createProduct() {
    this.productService.create(this.product).subscribe(() => {
      this.productService.showMessage('Livro criado');
      this.router.navigate(['/books']);
    });
  }

  cancel() {
    this.router.navigate(['/books']); // router para voltar à tela anterior
  }

}
