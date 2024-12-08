package com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class MenuResponseModel {

     String menuId;
     String name;
     String description;
     Double price;
     String category;
     String itemImage;

     Status status;
}
