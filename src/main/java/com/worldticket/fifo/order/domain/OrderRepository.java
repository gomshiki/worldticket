package com.worldticket.fifo.order.domain;

import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.order.infra.OrdersStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Orders, Long> {
    List<Orders> findByMember(Member member);

    Optional<Orders> findByOrdersIdAndOrdersStatus(Long orderId, OrdersStatus ordersStatus);
}
