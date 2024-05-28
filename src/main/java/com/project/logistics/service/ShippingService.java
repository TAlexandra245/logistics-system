package com.project.logistics.service;

import com.project.logistics.config.CompanyInfo;
import com.project.logistics.dao.Destination;
import com.project.logistics.dao.Order;
import com.project.logistics.dao.OrderStatus;
import com.project.logistics.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {


    private final CompanyInfo companyInfo;
    private final OrderRepository orderRepository;
    private final ShippingManager shippingManager;

    public String advanceDate() {

        LocalDate currentDate = companyInfo.advanceDate();
        log.info("New Day starting: " + currentDate);

        Map<Destination, List<Long>> ordersByDestinationId = orderRepository.findAllByDeliveryDate(companyInfo.getCurrentDateAsLong())
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.NEW)
                .collect(Collectors.groupingBy(Order::getDestination, Collectors.mapping(Order::getId, Collectors.toList())));

        List<Long> orderIds = ordersByDestinationId.values().stream().flatMap(java.util.Collection::stream).toList();

        orderRepository.updateStatusForOrders(orderIds, null, OrderStatus.DELIVERING);

        String destinationName = ordersByDestinationId.keySet()
                .stream()
                .map(Destination::getName)
                .collect(Collectors.joining(", "));

        log.info("Today we will be delivering to " + destinationName);

        for (Map.Entry<Destination, List<Long>> entry : ordersByDestinationId.entrySet()) {
            shippingManager.deliverToDestination(entry.getKey(), entry.getValue());
        }
        return String.format("Today we will be delivering to %s", destinationName);
    }

}
