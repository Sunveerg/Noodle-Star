package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import reactor.core.publisher.Flux;

public interface MenuService {

    Flux<MenuResponseModel> getAllMenu();
}
