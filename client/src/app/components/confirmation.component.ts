import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { OrderResponse } from '../models';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {

  private restaurantSvc = inject(RestaurantService);

  protected paymentReceipt!: OrderResponse;

  // TODO: Task 5
  ngOnInit(): void {
    this.paymentReceipt = this.restaurantSvc.paymentReceipt;
  }

}
