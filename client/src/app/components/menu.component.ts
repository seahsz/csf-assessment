import { Component, inject, OnChanges, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Observable, tap } from 'rxjs';
import { Item, Menu } from '../models';
import { OrderStore } from '../order.store';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  // TODO: Task 2
  private restSvc = inject(RestaurantService);
  private orderStore = inject(OrderStore);

  protected menus$!: Observable<Menu[]>;

  protected currOrder$!: Observable<Item[]>;
 
  protected totalQty = 0;
  protected totalPrice = 0;

  ngOnInit(): void {
    console.info("In onInit: Loading menus");
    this.menus$ = this.restSvc.getMenuItems().pipe(
      tap((menus: Menu[]) => console.info(menus))
    );
    this.currOrder$ = this.orderStore.getItems.pipe(
      tap((items: Item[]) => {
        console.info("Current order: ", items);
        let currQty = 0;
        let currPrice = 0;
        items.forEach(item => {
          currQty = currQty + item.quantity;
          currPrice = currPrice + (item.quantity * item.price);
        });
        // set component property
        this.totalQty = currQty;
        this.totalPrice = currPrice;
      })
    );
  }

  addItem(itemId: string, itemPrice: number, itemName: string) {
    const newItem: Item = {
      id: itemId,
      name: itemName,
      price: itemPrice,
      quantity: 1
    }
    this.orderStore.addItem(newItem);
  }

  delItem(itemId: string) {
    this.orderStore.delItem(itemId);
  }


}
