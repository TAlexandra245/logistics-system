package com.project.logistics.dto;

import com.project.logistics.dao.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    private Long id;

    private Long deliveryDate;

    private Long lastUpdated;

    private OrderStatus orderStatus;

    private Long destinationId;
}
