package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.FoodOrderItems;
import com.joysistvi.CyberAccess.repo.FoodOrderItemsRepo;

import java.util.List;

public class FoodOrderItemsServiceImpl
        implements FoodOrderItemsService {

    private final FoodOrderItemsRepo repo;

    public FoodOrderItemsServiceImpl(
            FoodOrderItemsRepo repo) {

        this.repo = repo;
    }

    @Override
    public List<FoodOrderItems> getAll() {
        return repo.getAll();
    }

    @Override
    public List<FoodOrderItems> getByOrderId(int orderId) {

        if (orderId <= 0) {
            return List.of();
        }

        return repo.getByOrderId(orderId);
    }

    @Override
    public boolean add(FoodOrderItems item) {

        if (item == null) {
            return false;
        }

        if (item.getOrderId() <= 0) {
            System.out.println("Invalid order ID.");
            return false;
        }

        if (item.getFoodItemId() <= 0) {
            System.out.println("Invalid food item ID.");
            return false;
        }

        if (item.getQuantity() <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return false;
        }

        if (item.getUnitPrice() < 0) {
            System.out.println("Invalid unit price.");
            return false;
        }

        return repo.add(item);
    }

    @Override
    public boolean delete(int id) {

        if (id <= 0) {
            return false;
        }

        return repo.delete(id);
    }
}