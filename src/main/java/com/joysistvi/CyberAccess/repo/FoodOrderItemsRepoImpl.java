package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.FoodOrderItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodOrderItemsRepoImpl implements FoodOrderItemsRepo {

    private final DatabaseConnection databaseConnection;

    public FoodOrderItemsRepoImpl(
            DatabaseConnection databaseConnection) {

        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<FoodOrderItems> getAll() {

        List<FoodOrderItems> items = new ArrayList<>();

        String sql = """
                SELECT id, order_id, food_item_id,
                       quantity, unit_price
                FROM food_order_items
                ORDER BY id DESC
                """;

        try (Connection connection = databaseConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                items.add(new FoodOrderItems(
                        resultSet.getInt("id"),
                        resultSet.getInt("order_id"),
                        resultSet.getInt("food_item_id"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("unit_price")
                ));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading food order items: "
                            + e.getMessage()
            );
        }

        return items;
    }

    @Override
    public List<FoodOrderItems> getByOrderId(int orderId) {

        List<FoodOrderItems> items = new ArrayList<>();

        String sql = """
                SELECT id, order_id, food_item_id,
                       quantity, unit_price
                FROM food_order_items
                WHERE order_id = ?
                ORDER BY id
                """;

        try (Connection connection = databaseConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    items.add(new FoodOrderItems(
                            resultSet.getInt("id"),
                            resultSet.getInt("order_id"),
                            resultSet.getInt("food_item_id"),
                            resultSet.getInt("quantity"),
                            resultSet.getDouble("unit_price")
                    ));
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading order items: "
                            + e.getMessage()
            );
        }

        return items;
    }

    @Override
    public boolean add(FoodOrderItems item) {

        String sql = """
                INSERT INTO food_order_items
                (order_id, food_item_id, quantity, unit_price)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = databaseConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, item.getOrderId());
            statement.setInt(2, item.getFoodItemId());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getUnitPrice());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding food order item: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        String sql =
                "DELETE FROM food_order_items WHERE id = ?";

        try (Connection connection = databaseConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error deleting food order item: "
                            + e.getMessage()
            );
        }

        return false;
    }
}