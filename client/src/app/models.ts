// You may use this file to create any models
export interface Menu {
    id: string,
    name: string,
    description: string,
    price: number
}

export interface Item {
    id: string,
    name: string,
    quantity: number, 
    price: number
}

export interface OrderSlice {
    items: Item[]
}

export interface OrderPayload {
    username: string,
    password: string,
    items: Item[]
}

export interface OrderResponse {
    orderId: string,
    paymentId: string,
    total: number,
    timestamp: number
}


