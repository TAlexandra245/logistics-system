package com.project.logistics.service;

import com.project.logistics.config.CompanyInfo;
import com.project.logistics.dao.Destination;
import com.project.logistics.dao.Order;
import com.project.logistics.dao.OrderStatus;
import com.project.logistics.dto.OrderDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.repository.DestinationRepository;
import com.project.logistics.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;
    private final CompanyInfo companyInfo;

    public void addOrders(List<OrderDto> orderDtos) throws CanNotCreateEntity {
        Map<Long, Destination> destinationMap = destinationRepository.findAll().stream()
                .collect(Collectors.toMap(Destination::getId, Function.identity()));

        validateOrdersPayload(orderDtos, destinationMap.keySet());

        List<Order> ordersToSave = new ArrayList<>();
        orderDtos.forEach(orderDto -> ordersToSave.add(
                buildOrderFromOrderDto(orderDto.getDeliveryDate(), destinationMap.get(orderDto.getDestinationId())))
        );

        orderRepository.saveAll(ordersToSave);

    }

    private void validateOrdersPayload(List<OrderDto> orderDtos, Set<Long> destinationIds) throws CanNotCreateEntity {
        StringBuilder error = new StringBuilder();
        for (int i = 0; i < orderDtos.size(); i++) {
            OrderDto orderDto = orderDtos.get(i);
            if (!destinationIds.contains(orderDto.getDestinationId())) {
                error.append(String.format("Destination id %d is invalid for order %d\n", orderDto.getDestinationId(), i));
            }
            if (orderDto.getDeliveryDate() <= companyInfo.getCurrentDateAsLong()) {
                error.append(String.format("Destination date %d is invalid for order %d\n", orderDto.getDestinationId(), i));

            }
        }

        if (!error.toString().isBlank()) {
            throw new CanNotCreateEntity(error.toString());
        }
    }

    private Order buildOrderFromOrderDto(Long deliveryDate, Destination destination) {
        return Order.builder()
                .orderStatus(OrderStatus.NEW)
                .lastUpdated(System.currentTimeMillis())
                .deliveryDate(deliveryDate)
                .destination(destination)
                .build();

    }

}
