import { Status } from "./Status";

export interface menuResponseModel {
    menuId: number;
    name: string;
    description: string;
    price: number;
    category: string;
    itemImage: string;
  
    status: Status;
  }
  