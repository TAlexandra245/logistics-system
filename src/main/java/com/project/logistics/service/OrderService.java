package com.project.logistics.service;

import com.project.logistics.config.CompanyInfo;
import com.project.logistics.dao.Destination;
import com.project.logistics.dao.Order;
import com.project.logistics.dao.OrderStatus;
import com.project.logistics.dto.AddOrderDto;
import com.project.logistics.dto.OrderConverter;
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

    public List<OrderDto> addOrders(List<AddOrderDto> addOrderDtos) throws CanNotCreateEntity {
        Map<Long, Destination> destinationMap = destinationRepository.findAll().stream()
                .collect(Collectors.toMap(Destination::getId, Function.identity()));

        validateOrdersPayload(addOrderDtos, destinationMap.keySet());

        List<Order> ordersToSave = new ArrayList<>();
        addOrderDtos.forEach(addOrderDto -> ordersToSave.add(
                buildOrderFromOrderDto(addOrderDto.getDeliveryDate(), destinationMap.get(addOrderDto.getDestinationId())))
        );

        return OrderConverter.entityListToDtoList(ordersToSave);

    }

    public void cancelOrders(List<Long> orderIds) {

        List<Order> foundOrders = orderRepository.findAllById(orderIds);
        for (Order foundOrder : foundOrders) {
            if (foundOrder.getOrderStatus() != OrderStatus.DELIVERED) {
                foundOrder.setOrderStatus(OrderStatus.CANCELED);
            }
        }

        orderRepository.saveAll(foundOrders);
    }

    public List<OrderDto> getOrders(String dateAsString, String destination) {

        Long dateAsLong = companyInfo.getCurrentDateAsLong();

        if(!dateAsString.isBlank()) {
            dateAsLong = companyInfo.getLocalDateAsStringLong(dateAsString);
        }

        List<Order> foundOrders =
                orderRepository.findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(dateAsLong, destination);
        return OrderConverter.entityListToDtoList(foundOrders);
    }

    private void validateOrdersPayload(List<AddOrderDto> addOrderDtos, Set<Long> destinationIds) throws CanNotCreateEntity {
        StringBuilder error = new StringBuilder();
        for (int i = 0; i < addOrderDtos.size(); i++) {
            AddOrderDto addOrderDto = addOrderDtos.get(i);
            if (!destinationIds.contains(addOrderDto.getDestinationId())) {
                error.append(String.format("Destination id %d is invalid for order %d\n", addOrderDto.getDestinationId(), i));
            }
            if (addOrderDto.getDeliveryDate() <= companyInfo.getCurrentDateAsLong()) {
                error.append(String.format("Destination date %d is invalid for order %d\n", addOrderDto.getDestinationId(), i));

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
