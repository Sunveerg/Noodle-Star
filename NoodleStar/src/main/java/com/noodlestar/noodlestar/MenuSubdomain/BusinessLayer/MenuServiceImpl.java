package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;



import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.DishNameAlreadyExistsException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.InvalidDishDescriptionException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.InvalidDishNameException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.InvalidDishPriceException;
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
    public MenuResponseModel addDish(MenuRequestModel menuRequestModel) {
        validateMenuRequest(menuRequestModel);

        menuRepository.findByName(menuRequestModel.getName())
                .map(existingDish -> {
                    throw new DishNameAlreadyExistsException("Dish with name '" + menuRequestModel.getName() + "' already exists.");
                })
                .block();

        Menu menuEntity = new Menu();
        menuEntity.setName(menuRequestModel.getName());
        menuEntity.setDescription(menuRequestModel.getDescription());
        menuEntity.setPrice(menuRequestModel.getPrice());
        menuEntity.setCategory(menuRequestModel.getCategory());
        menuEntity.setItemImage(menuRequestModel.getItemImage());
        menuEntity.setStatus(Status.AVAILABLE);
        menuEntity.setMenuId(UUID.randomUUID().toString());

        Menu savedMenu = menuRepository.save(menuEntity).block();
        if (savedMenu == null) {
            throw new RuntimeException("Failed to save the dish to the database");
        }

        MenuResponseModel response = new MenuResponseModel();
        response.setMenuId(savedMenu.getMenuId());
        response.setName(savedMenu.getName());
        response.setDescription(savedMenu.getDescription());
        response.setPrice(savedMenu.getPrice());
        response.setCategory(savedMenu.getCategory());
        response.setItemImage(savedMenu.getItemImage());
        response.setStatus(savedMenu.getStatus());

        log.info("Dish added successfully with ID: {}", response.getMenuId());
        return response;
    }

    private void validateMenuRequest(MenuRequestModel menuRequest) {
        if (menuRequest.getName() == null || menuRequest.getName().isEmpty()) {
            throw new InvalidDishNameException("Dish name cannot be null or empty");
        }
        if (menuRequest.getPrice() == null || menuRequest.getPrice().doubleValue() <= 0) {
            throw new InvalidDishPriceException("Dish price must be greater than 0");
        }
        if (menuRequest.getDescription() == null || menuRequest.getDescription().isEmpty()) {
            throw new InvalidDishDescriptionException("Dish description cannot be null or empty");
        }
    }

}
public Mono<MenuResponseModel> getMenuById(String menuId) {
    return menuRepository.findMenuByMenuId(menuId)
            .map(EntityDTOUtil::toMenuResponseDTO);
}
}