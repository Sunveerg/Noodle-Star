package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;



import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.utils.EntityDTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<MenuResponseModel> addDish(Mono<MenuRequestModel> menuRequestMono) {
        return menuRequestMono
                .flatMap(this::validateMenuRequest) // Validate the incoming request
                .map(EntityDTOUtil::toMenuEntity) // Convert request model to entity
                .doOnNext(menu -> menu.setMenuId(EntityDTOUtil.generateMenuIdString())) // Generate unique menu ID
                .flatMap(menuRepository::save) // Save the entity to the repository
                .map(EntityDTOUtil::toMenuResponseDTO) // Convert the saved entity to a response model
                .doOnSuccess(response -> log.info("Dish added successfully with ID: {}", response.getMenuId()))
                .doOnError(error -> log.error("Error adding dish: {}", error.getMessage()));
    }

    private Mono<MenuRequestModel> validateMenuRequest(MenuRequestModel menuRequest) {
        if (menuRequest.getName() == null || menuRequest.getName().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Dish name cannot be null or empty"));
        }
        if (menuRequest.getPrice() == null || menuRequest.getPrice().doubleValue() <= 0) {
            return Mono.error(new IllegalArgumentException("Dish price must be greater than 0"));
        }
        if (menuRequest.getDescription() == null || menuRequest.getDescription().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Dish description cannot be null or empty"));
        }
        return Mono.just(menuRequest);
    }


}
