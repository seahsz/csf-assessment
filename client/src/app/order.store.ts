import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Item, OrderSlice } from "./models";

const INIT: OrderSlice = {
    items: []
}

@Injectable()
export class OrderStore extends ComponentStore<OrderSlice> {
    constructor() {
        // Initialize the store, initally the store is empty
        super(INIT)
    }

    // Mutators
    readonly addItem = this.updater<Item>(
        (slice: OrderSlice, newItem: Item) => {
            let added = false;
            slice.items.forEach(item => {
                if (item.id === newItem.id) {
                    item.quantity = item.quantity + 1;
                    added = true;
                }
            });
            if (!added) {
                return {
                    items: [...slice.items, newItem]
                } as OrderSlice
            } else {
                return {
                    items: [...slice.items]
                } as OrderSlice
            }

        }
    )

    readonly delItem = this.updater<string>(
        (slice: OrderSlice, itemId: string) => {
            slice.items.forEach(item => {
                if (item.id === itemId) {
                    item.quantity = item.quantity - 1
                }
            });
            return {
                items: slice.items.filter(i => i.quantity > 0),
            }
        }
    )

    readonly clearStore = this.updater(
        (slice: OrderSlice) => {
            return {
                items: []
            } as OrderSlice
        }
    ) 

    // Selector
    readonly getItems = this.select<Item[]>(
        (slice: OrderSlice) => slice.items
    )

}