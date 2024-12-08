package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;



import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.NotFoundException;
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
    public Mono<MenuResponseModel> getMenuById(String menuId) {
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

}
