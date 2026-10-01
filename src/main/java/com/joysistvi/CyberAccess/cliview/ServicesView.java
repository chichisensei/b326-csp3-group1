package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.ServicesController;
import java.sql.SQLException;

public class ServicesView {

    private final ServicesController controller;
    private final ConsoleInput input;
    public ServicesView(ServicesController controller, ConsoleInput input) { this.controller = controller; this.input = input; }
    public void show() {
        while (true) {
            System.out.println("\nSERVICES: 1 List | 2 Add | 3 Edit | 4 Activate | 5 Deactivate | 0 Back");
            try {
                switch (input.number("Choose: ")) {
                    case 0 -> { return; }
                    case 1 -> list();
                    case 2 -> System.out.println("Created service #" + controller.create(input.text("Name: "), input.text("Description: ")));
                    case 3 -> {
                        controller.update(input.number("Service ID: "), input.text("New name: "), input.text("New description: "));
                        System.out.println("Service updated.");
                    }
                    case 4 -> { controller.setStatus(input.number("Service ID: "), "Active"); System.out.println("Service activated."); }
                    case 5 -> { controller.setStatus(input.number("Service ID: "), "Inactive"); System.out.println("Service deactivated."); }
                    default -> System.out.println("Choose a listed option.");
                }
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
            catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
        }
    }
    public void list() throws SQLException {
        var rows = controller.list();
        if (rows.isEmpty()) System.out.println("No services yet. Choose Add first.");
        for (var row : rows) System.out.printf("#%d | %s | %s | %s%n", row.id(), row.name(), row.description(), row.status());
    }

}
