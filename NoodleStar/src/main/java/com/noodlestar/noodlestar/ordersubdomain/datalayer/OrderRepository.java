package com.noodlestar.noodlestar.ordersubdomain.datalayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}

