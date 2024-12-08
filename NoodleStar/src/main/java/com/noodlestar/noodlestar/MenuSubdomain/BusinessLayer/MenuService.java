package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuService {

    Flux<MenuResponseModel> getAllMenu();

    MenuResponseModel addDish(MenuRequestModel menuRequestModel);
    Mono<MenuResponseModel> getMenuById(String menuId);
}
