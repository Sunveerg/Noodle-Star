package com.noodlestar.noodlestar.MenuSubdomain.utils;


import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityDTOUtil {

    public static MenuResponseModel toMenuResponseDTO(Menu menu) {
        MenuResponseModel menuResponseModel  = new MenuResponseModel ();
        BeanUtils.copyProperties(menu, menuResponseModel);
        return menuResponseModel;
    }

    public static Menu toMenuEntity(MenuRequestModel menuRequestModel){
        return Menu.builder()
                .menuId(generateMenuIdString())
                .name(menuRequestModel.getName())
                .description(menuRequestModel.getDescription())
                .price(menuRequestModel.getPrice())
                .category(menuRequestModel.getCategory())
                .ItemImage(menuRequestModel.getItemImage())
                .status(menuRequestModel.getStatus())
                .build();
    }

    public static String generateMenuIdString() {
        return UUID.randomUUID().toString();
    }
}
