package com.worldticket.fifo.order.domain;

import org.springframework.data.repository.CrudRepository;

public interface OrdersLineRepository extends CrudRepository<OrdersLine, Long> {
}
