package com.project.logistics.service;

import com.project.logistics.config.CompanyInfo;
import com.project.logistics.dao.Destination;
import com.project.logistics.dao.Order;
import com.project.logistics.repository.DestinationRepository;
import com.project.logistics.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final CompanyInfo companyInfo;
    private final OrderRepository orderRepository;
    private final DestinationRepository destinationRepository;

    private String advanceDate() {

        companyInfo.advanceDate();

        List<Order> ordersForToday = orderRepository.findAllByDeliveryDate(companyInfo.getCurrentDateAsLong());

        for(Order order : ordersForToday) {

        }
        Map<Destination, List<Order>> ordersByDestination = orderRepository.findAllByDeliveryDate(companyInfo.getCurrentDateAsLong())
                .stream()
                .collect(Collectors.groupingBy(Order::getDestination));

        return null;
    }
}
