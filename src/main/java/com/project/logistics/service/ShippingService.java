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
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {


    private final CompanyInfo companyInfo;
    private final OrderRepository orderRepository;
    private final ExecutorService executorService;
    private final ShippingManager shippingManager;

    public String advanceDate() {

        LocalDate currentDate = companyInfo.advanceDate();
        log.info("New Day starting: " + currentDate);

        List<Order> ordersForToday = orderRepository.findAllByDeliveryDate(companyInfo.getCurrentDateAsLong());

        for (Order order : ordersForToday) {
            orderRepository.changeOrderState(order, OrderStatus.DELIVERING);
        }

        orderRepository.saveAll(ordersForToday);


        Map<Destination, List<Order>> ordersByDestination = orderRepository.findAllByDeliveryDate(companyInfo.getCurrentDateAsLong())
                .stream()
                .collect(Collectors.groupingBy(Order::getDestination));

        String destinationName = ordersByDestination.keySet().stream().map(Destination::getName).collect(Collectors.joining(", "));

        log.info("Today we will be delivering to " + destinationName);

        for (Map.Entry<Destination, List<Order>> entry : ordersByDestination.entrySet()) {
//            DeliveryTask deliveryTask = new DeliveryTask(entry.getKey(), entry.getValue());
//            executorService.submit(deliveryTask);
            shippingManager.deliverToDestination(entry.getKey(), entry.getValue());
        }
        return String.format("Today we will be delivering to %s", destinationName);
    }

}
