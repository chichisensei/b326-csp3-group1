package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.FoodOrderItems;
import com.joysistvi.CyberAccess.service.FoodOrderItemsService;

import java.util.List;

public class FoodOrderItemsController {

    private final FoodOrderItemsService service;

    public FoodOrderItemsController(
            FoodOrderItemsService service) {

        this.service = service;
    }

    public List<FoodOrderItems> handleGetAll() {
        return service.getAll();
    }

    public List<FoodOrderItems> handleGetByOrderId(
            int orderId) {

        return service.getByOrderId(orderId);
    }

    public boolean handleAdd(FoodOrderItems item) {
        return service.add(item);
    }

    public boolean handleDelete(int id) {
        return service.delete(id);
    }
}