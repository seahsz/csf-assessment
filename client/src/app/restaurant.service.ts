import { inject, Injectable } from "@angular/core";
import { lastValueFrom, Observable, tap } from "rxjs";
import { Item, Menu, OrderPayload, OrderResponse } from "./models";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable()
export class RestaurantService {

  private http = inject(HttpClient);

  // Save the payment receipt here (Task 4/5)
  paymentReceipt!: OrderResponse;

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems(): Observable<Menu[]> {
    const headers = new HttpHeaders()
      .append("Content-Type", "application/json")
      .append("Accept", "application/json");
    return this.http.get<Menu[]>("/api/menu", { headers });
  }

  // TODO: Task 3.2
  sendOrder(order: Item[], username: string, password: string): Promise<OrderResponse> {
    const headers = new HttpHeaders()
      .append("Content-Type", "application/json")
      .append("Accept", "application/json");
    
    const payload: OrderPayload = {
      username: username,
      password: password,
      items: order
    }

    console.info("Sending order to backend");
    return lastValueFrom(
      this.http.post<OrderResponse>("/api/food_order", payload, { headers }).pipe(
          tap((value: OrderResponse) => {
            console.info("Receiving payment receipt from backend: ", value);
            this.paymentReceipt = value
          })
      )
    )
  }
}
