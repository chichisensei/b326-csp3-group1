package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.FoodOrderItems;

import java.util.List;

public interface FoodOrderItemsService {

    List<FoodOrderItems> getAll();

    List<FoodOrderItems> getByOrderId(int orderId);

    boolean add(FoodOrderItems item);

    boolean delete(int id);
}