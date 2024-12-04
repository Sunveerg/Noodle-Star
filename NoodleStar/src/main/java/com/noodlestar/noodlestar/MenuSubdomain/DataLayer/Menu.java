package com.noodlestar.noodlestar.MenuSubdomain.DataLayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Menu {


    @Id
    private String id;
    private String menuId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String itemImage;
    private Status status;


}
