package com.project.logistics.service.runnable;

import com.project.logistics.dao.Destination;
import com.project.logistics.dao.Order;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DeliveryTask implements Runnable {

    private final Destination destination;
    private final List<Order> orderList;

    @SneakyThrows
    @Override
    public void run() {

    }

}
