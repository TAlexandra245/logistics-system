package com.project.logistics.controller;

import com.project.logistics.dao.Order;
import com.project.logistics.dto.OrderDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    public void addOrders(@RequestBody @Valid List<OrderDto> orderDtos) throws CanNotCreateEntity {
        orderService.addOrders(orderDtos);
    }
}
