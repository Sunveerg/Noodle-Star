package com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer.MenuService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/menu")
@Validated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @GetMapping(value="")
    public Flux<MenuResponseModel> getAllMenu(){

        return menuService.getAllMenu();
    }

    @GetMapping(value = "/{menuId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<MenuResponseModel>> getMenuById(@PathVariable String menuId) {
        return menuService.getMenuById(menuId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("")
    public Mono<MenuResponseModel> addDish(@RequestBody MenuRequestModel menuRequestModel) {
        MenuResponseModel response = menuService.addDish(menuRequestModel);
        return Mono.just(response);
    }
}
