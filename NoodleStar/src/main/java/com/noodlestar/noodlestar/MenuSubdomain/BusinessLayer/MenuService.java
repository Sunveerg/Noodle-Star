package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuService {

    Flux<MenuResponseModel> getAllMenu();

    Mono<MenuResponseModel> getMenuById(String menuId);
}
