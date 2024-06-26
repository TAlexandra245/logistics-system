package com.project.logistics.service;

import com.project.logistics.config.CompanyInfo;
import com.project.logistics.dao.Destination;
import com.project.logistics.dao.OrderStatus;
import com.project.logistics.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShippingManager {

    private final OrderRepository orderRepository;
    private final CompanyInfo companyInfo;

    @SneakyThrows
    @Async("executor")
    public void deliverToDestination(Destination destination, List<Long> orderIds) {

        log.info(String.format("Started delivering to  %s  on %s for %d km",
                destination.getName(), Thread.currentThread().getName(), destination.getDistance()));

        Thread.sleep(destination.getDistance() * 1000);


        int noOfDeliveredOrders = orderRepository.updateStatusForOrders(orderIds,OrderStatus.DELIVERING, OrderStatus.DELIVERED);

        log.info(String.format("%d deliveries completed for %s", noOfDeliveredOrders, destination.getName()));

        long profit = companyInfo.calculateCompanyProfit((long) noOfDeliveredOrders * destination.getDistance());

        log.info("Company profit is " + profit);
    }
}
