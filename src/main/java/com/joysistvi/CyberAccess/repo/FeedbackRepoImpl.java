package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepoImpl implements FeedbackRepo {

    private final DatabaseConnection databaseConnection;

    public FeedbackRepoImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<Feedback> getAllFeedback() {

        List<Feedback> feedbackList = new ArrayList<>();

        String query = "SELECT id, comment, user_id " +
                "FROM feedback ORDER BY id DESC";

        try (Connection connection = databaseConnection.connect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                feedbackList.add(new Feedback(
                        resultSet.getInt("id"),
                        resultSet.getString("comment"),
                        resultSet.getInt("user_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("View Feedback Error: " + e.getMessage());
        }

        return feedbackList;
    }

    @Override
    public boolean addFeedback(Feedback feedback) {

        String query =
                "INSERT INTO feedback (comment, user_id) VALUES (?, ?)";

        try (Connection connection = databaseConnection.connect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, feedback.getComment());
            preparedStatement.setInt(2, feedback.getUserId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add Feedback Error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteFeedback(int id) {

        String query = "DELETE FROM feedback WHERE id = ?";

        try (Connection connection = databaseConnection.connect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete Feedback Error: " + e.getMessage());
        }

        return false;
    }
}