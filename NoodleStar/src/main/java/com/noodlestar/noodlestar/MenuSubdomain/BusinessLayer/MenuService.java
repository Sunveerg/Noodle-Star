package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuService {

    Flux<MenuResponseModel> getAllMenu();
    Mono<MenuResponseModel> addDish(Mono<MenuRequestModel> menuRequestModel);
    Mono<MenuResponseModel> getMenuById(String menuId);

    Mono<MenuResponseModel> updateMenu(Mono<MenuRequestModel> menuRequestModel, String menuId);

}
