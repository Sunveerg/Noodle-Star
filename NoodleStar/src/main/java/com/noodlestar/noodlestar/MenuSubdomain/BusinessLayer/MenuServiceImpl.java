package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;



import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;

import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishDescriptionException;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishNameException;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishPriceException;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }


    @Override
    public Flux<MenuResponseModel> getAllMenu() {
        return menuRepository.findAll()
                .map(EntityDTOUtil::toMenuResponseDTO);
    }

    @Override
    public Mono<MenuResponseModel> getMenuItemById(String menuId) {
        return menuRepository.findMenuByMenuId(menuId)
                .map(EntityDTOUtil::toMenuResponseDTO);
    }

    @Override
    public Mono<MenuResponseModel> updateMenu(Mono<MenuRequestModel> menuRequestModel, String menuId) {
        return menuRepository.findMenuByMenuId(menuId)
                .flatMap(existingMenu -> menuRequestModel.map(requestModel -> {
                    existingMenu.setName(requestModel.getName());
                    existingMenu.setDescription(requestModel.getDescription());
                    existingMenu.setPrice(requestModel.getPrice());
                    existingMenu.setCategory(requestModel.getCategory());
                    existingMenu.setItemImage(requestModel.getItemImage());
                    existingMenu.setStatus(requestModel.getStatus());
                    return existingMenu;
                }))
                .switchIfEmpty(Mono.error(new NotFoundException("Menu not found with id: " + menuId)))
                .flatMap(menuRepository::save)
                .map(EntityDTOUtil::toMenuResponseDTO);
    }

    @Override
    public Mono<MenuResponseModel> addDish(Mono<MenuRequestModel> menuRequestModel) {
        return menuRequestModel
                .map(EntityDTOUtil::toMenuEntity)
                .doOnNext(menu -> {
                    if (menu.getName() == null || menu.getName().isEmpty()) {
                        throw new InvalidDishNameException("Dish name cannot be null or empty.");
                    }
                    if (menu.getPrice() == null || menu.getPrice().doubleValue() <= 0) {
                        throw new InvalidDishPriceException("Dish price must be greater than 0.");
                    }
                    if (menu.getDescription() == null || menu.getDescription().isEmpty()) {
                        throw new InvalidDishDescriptionException("Dish description cannot be null or empty.");
                    }
                    menu.setMenuId(UUID.randomUUID().toString());
                    menu.setStatus(Status.AVAILABLE);
                })
                .flatMap(menuRepository::insert)
                .flatMap(savedMenu -> menuRepository.findByName(savedMenu.getName())
                        .map(EntityDTOUtil::toMenuResponseDTO))
                .doOnSuccess(response -> log.info("Dish added successfully with ID: {}", response.getMenuId()));
    }

    @Override
    public Mono<Void> deleteMenuItem(String menuId) {
        return menuRepository.findMenuByMenuId(menuId)
                .switchIfEmpty(Mono.error(new NotFoundException("Dish with ID '" + menuId + "' not found.")))
                .flatMap(menuRepository::delete);
    }

    @Override
    public Mono<MenuResponseModel> getMenuByName(String name) {
        return menuRepository.findByName(name) // Directly using the Mono version
                .map(EntityDTOUtil::toMenuResponseDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("No menu item found with name: " + name)));
    }

}
