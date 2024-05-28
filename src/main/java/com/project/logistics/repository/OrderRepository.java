package com.project.logistics.repository;

import com.project.logistics.dao.Order;
import com.project.logistics.dao.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDestinationId(Long destinationId);

    List<Order> findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(Long deliveryDate, String destinationQueryString);

    List<Order> findAllByDeliveryDate(Long deliveryDate);


    default void archiveOrder(Order order) {
        order.setDestination(null);
        changeOrderState(order, OrderStatus.ARCHIVED);
        this.save(order);
    }

    default boolean changeOrderState(Order order, OrderStatus newStatus) {
        OrderStatus initialStatus = order.getOrderStatus();

        if (OrderStatus.allowedTransitions.get(initialStatus).contains(newStatus)) {
            order.setOrderStatus(newStatus);
            return true;
        }
        return false;
    }

    default int updateStatusForOrders(List<Long> ordersIds, OrderStatus initialStatus, OrderStatus newStatus) {

        int affectedRows = 0;

        List<Order> ordersToUpdate = this.findAllById(ordersIds).stream().filter(order -> isNull(initialStatus) || order.getOrderStatus() == initialStatus).toList();

        for (Order order : ordersToUpdate) {
            if (changeOrderState(order, newStatus)) {
                affectedRows++;
            }
        }
        this.saveAll(ordersToUpdate);
        return affectedRows;
    }
}
