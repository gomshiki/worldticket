package com.worldticket.domain;

import com.worldticket.infra.OrdersStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Orders, Long> {
    List<Orders> findByMember(Member member);

    Optional<Orders> findByOrdersIdAndOrdersStatus(Long orderId, OrdersStatus ordersStatus);
}
