package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.FoodOrderItems;

import java.util.List;

public interface FoodOrderItemsRepo {

    List<FoodOrderItems> getAll();

    List<FoodOrderItems> getByOrderId(int orderId);

    boolean add(FoodOrderItems item);

    boolean delete(int id);
}