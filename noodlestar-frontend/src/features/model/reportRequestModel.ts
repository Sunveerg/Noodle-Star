export interface ReportRequestModel {
  reportType: string; // e.g., "Most Popular Menu Item"

  menuItemName: string; // The most popular menu item

  itemCount: number; // The number of times this item was ordered
}
