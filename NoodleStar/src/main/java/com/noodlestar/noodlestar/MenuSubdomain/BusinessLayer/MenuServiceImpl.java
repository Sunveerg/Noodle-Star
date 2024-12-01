package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;



import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.utils.EntityDTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
}
