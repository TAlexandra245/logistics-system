package com.project.logistics.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public enum OrderStatus {

    NEW,
    ARCHIVED,
    DELIVERING,
    DELIVERED,
    CANCELED;


    public static Map<OrderStatus, List<OrderStatus>> allowedTransitions;

    static {
        allowedTransitions.put(NEW, List.of(DELIVERING, CANCELED, ARCHIVED));
        allowedTransitions.put(DELIVERED, List.of(ARCHIVED));
        allowedTransitions.put(DELIVERING, List.of(DELIVERED, CANCELED, ARCHIVED));
        allowedTransitions.put(CANCELED, List.of(NEW, ARCHIVED));
        allowedTransitions.put(ARCHIVED, Collections.emptyList());
    }
}
