package com.joysistvi.CyberAccess;

import com.joysistvi.CyberAccess.cliview.*;
import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.controller.*;
import com.joysistvi.CyberAccess.repo.*;
import com.joysistvi.CyberAccess.service.*;
import java.sql.SQLException;
import java.util.Scanner;

// Standalone staff/demo menu; the group's main menu can reuse these controllers and views.
// Starts the menus and connects the views, controllers, services and repositories
public class ModuleApp {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try (var connection = db.connect()) {
            if (!connection.isValid(5)) throw new SQLException("Database connection is not valid.");
        } catch (SQLException e) {
            System.err.println("Cannot connect. Start MySQL, import your database dump and database/01_fix_transaction_id.sql, and check DB settings.\n" + e.getMessage());
            return;
        }
        ConsoleInput input = new ConsoleInput(new Scanner(System.in));
        ServicesView services = new ServicesView(new ServicesController(new ServicesServiceImpl(new ServicesRepoImpl(db))), input);
        BillView bills = new BillView(new BillController(new BillServiceImpl(new BillRepoImpl(db))));
        TransactionsView transactions = new TransactionsView(
                new TransactionsController(new TransactionsServiceImpl(new TransactionsRepoImpl(db))), bills, services, input);
        System.out.println("Cyber Access - Ava's staff/demo module (no login connected yet)");
        try {
            while (true) {
                System.out.println("\n1 Manage services | 2 Transactions / History / Bills | 0 Exit");
                switch (input.number("Choose: ")) {
                    case 0 -> { return; }
                    case 1 -> services.show();
                    case 2 -> transactions.show();
                    default -> System.out.println("Choose 0, 1, or 2.");
                }
            }
        } catch (ConsoleInput.EndOfInput e) { System.out.println("\nGoodbye."); }
    }
}
