package com.project.logistics.repository;

import com.project.logistics.dao.Order;
import com.project.logistics.dao.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDestinationId(Long destinationId);

    List<Order> findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(Long deliveryDate, String destinationQueryString);

    List<Order> findAllByDeliveryDate(Long deliveryDate);



    default void archiveOrder(Order order) {
        order.setDestination(null);
        order.setOrderStatus(OrderStatus.ARCHIVED);
        this.save(order);
    }
}
