package com.project.logistics.controller;

import com.project.logistics.dto.AddOrderDto;
import com.project.logistics.dto.OrderDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    public List<OrderDto> addOrders(@RequestBody @Valid List<AddOrderDto> addOrderDtos) throws CanNotCreateEntity {
        return orderService.addOrders(addOrderDtos);
    }

    @PutMapping ("/cancel")
    public void cancelOrders(@RequestBody List<Long> orderIds) {
       orderService.cancelOrders(orderIds);
    }

    @GetMapping("/status")
    public List<OrderDto> getOrders(@RequestParam(name = "date", required = false) String dateAsString, @RequestParam(name = "destination", required = false, defaultValue = "") String destinationQueryParam) {
        return orderService.getOrders(dateAsString, destinationQueryParam);
    }
}
