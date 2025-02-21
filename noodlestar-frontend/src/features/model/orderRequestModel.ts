export interface OrderDetailsRequestModel {
  menuId: string;
  quantity: number;
  price: number;
}

export interface OrderRequestModel {
  customerId: string;
  status?: string;
  orderDate?: string;
  orderDetails: OrderDetailsRequestModel[];
  total?: number;
}
