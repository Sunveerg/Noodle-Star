package com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("api/v1/menu")
@Validated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping(value = "")
    public Flux<MenuResponseModel> getAllMenu() {
        return menuService.getAllMenu();
    }

    @PostMapping("")
    public Mono<MenuResponseModel> addDish(@RequestBody MenuRequestModel menuRequestModel) {
        MenuResponseModel response = menuService.addDish(menuRequestModel);
        return Mono.just(response); // Wrap in Mono to maintain reactive response
    }
}
