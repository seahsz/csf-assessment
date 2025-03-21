import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrderStore } from '../order.store';
import { Router } from '@angular/router';
import { lastValueFrom, Observable, tap } from 'rxjs';
import { Item, OrderResponse } from '../models';
import { RestaurantService } from '../restaurant.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {

  // TODO: Task 3
  private fb = inject(FormBuilder);
  private orderStore = inject(OrderStore);
  private restaurantSvc = inject(RestaurantService);
  private router = inject(Router);

  protected loginForm!: FormGroup;

  currOrder$!: Observable<Item[]>;
  currOrder: Item[] = [];
  totalPrice = 0;

  ngOnInit(): void {
    this.loginForm = this.createLoginForm();
    this.currOrder$ = this.orderStore.getItems.pipe(
      tap( (items: Item[]) => {
        this.currOrder = items;
        let currPrice = 0;
        items.forEach(item => {
          currPrice = currPrice + (item.quantity * item.price);
        });
        // set component property
        this.totalPrice = currPrice;
      })
    )
  }

  createLoginForm(): FormGroup {
    return this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    })
  }

  submitForm() {
    console.info("Inside submit form");
    const username = this.loginForm.get("username")?.value;
    const password = this.loginForm.get("password")?.value;

    this.restaurantSvc.sendOrder(this.currOrder, username, password)
      .then((value: OrderResponse) => {
        this.loginForm.reset();
        this.orderStore.clearStore();
        this.router.navigate(['/confirmation']);
      })
      .catch((error: HttpErrorResponse) => {
        alert(error.error.message);
      });
    
  }

  startOver() {
    this.orderStore.clearStore();
    this.router.navigate(['/']);
  }

}
