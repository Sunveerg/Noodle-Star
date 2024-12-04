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

//    public static MenuResponseModel toMenuResponseDTO(Menu menu) {
//        MenuResponseModel menuResponseModel  = new MenuResponseModel ();
//        BeanUtils.copyProperties(menu, menuResponseModel);
//        return menuResponseModel;
//    }
//
//    public static Menu toMenuEntity(MenuRequestModel menuRequestModel){
//        return Menu.builder()
//                .menuId(generateMenuIdString())
//                .name(menuRequestModel.getName())
//                .description(menuRequestModel.getDescription())
//                .price(menuRequestModel.getPrice())
//                .category(menuRequestModel.getCategory())
//                .itemImage(menuRequestModel.getItemImage())
//                .status(menuRequestModel.getStatus())
//                .build();
//    }

    public static Menu toMenuEntity(MenuRequestModel menuRequest) {
        return Menu.builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .description(menuRequest.getDescription())
                .category(menuRequest.getCategory())
                .build();
    }

    public static MenuResponseModel toMenuResponseDTO(Menu menu) {
        return MenuResponseModel.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .category(menu.getCategory())
                .build();
    }

    public static MenuResponseModel menuEntityToResponseDTO(Menu menu) {
        return MenuResponseModel.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .itemImage(menu.getItemImage())
                .status(menu.getStatus())
                .build();
    }

    // Convert MenuRequestDTO to Menu entity
    public static Menu menuRequestDtoToEntity(MenuRequestModel dto) {
        return Menu.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .itemImage(dto.getItemImage())
                .status(dto.getStatus())
                .build();
    }

    public static String generateMenuIdString() {
        return UUID.randomUUID().toString();
    }
}
