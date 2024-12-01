package com.noodlestar.noodlestar.MenuSubdomain.DataLayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MenuRepository extends ReactiveMongoRepository<Menu, String> {
    Mono<Menu> findMenuByMenuId(String menuId);

}
