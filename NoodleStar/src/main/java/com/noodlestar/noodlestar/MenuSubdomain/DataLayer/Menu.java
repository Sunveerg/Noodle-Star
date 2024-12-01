package com.noodlestar.noodlestar.MenuSubdomain.DataLayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Menu {


    @Id
    private String id;
    private String menuId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String ItemImage;
    private Status status;


}
