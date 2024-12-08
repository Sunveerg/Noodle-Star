package com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer.MenuService;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
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
    public Mono<ResponseEntity<MenuResponseModel>> getMenuItemById(@PathVariable String menuId) {
        return menuService.getMenuItemById(menuId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("")
    public Mono<MenuResponseModel> addDish(@RequestBody MenuRequestModel menuRequestModel) {
        MenuResponseModel response = menuService.addDish(menuRequestModel);
        return Mono.just(response);
    }
    @DeleteMapping("/{menuId}")
    public Mono<ResponseEntity<Void>> deleteMenuItem(@PathVariable String menuId) {
        return menuService.deleteMenuItem(menuId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .onErrorResume(NotFoundException.class, e -> Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)));
    }

}
