package com.project.logistics.dto;

import com.project.logistics.dao.Order;

import java.util.List;

public class OrderConverter {


    public static OrderDto entityToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .deliveryDate(order.getDeliveryDate())
                .orderStatus(order.getOrderStatus())
                .destinationId(order.getDestination().getId())
                .lastUpdated(order.getLastUpdated())
                .build();
    }

    public static List<OrderDto> entityListToDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(OrderConverter::entityToDto)
                .toList();
    }
}
