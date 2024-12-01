import { Status } from "./Status";

export interface menuRequestModel {
    name: string;
    description: string;
    price: number;
    category: string;
    itemImage: string;
  
    status: Status;
  }
  