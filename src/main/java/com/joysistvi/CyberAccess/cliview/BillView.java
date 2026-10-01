package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.BillController;
import com.joysistvi.CyberAccess.model.Bill;
import java.sql.SQLException;

public class BillView {
    private final BillController controller;
    public BillView(BillController controller) { this.controller = controller; }
    public void show(int id) throws SQLException { System.out.print(format(controller.getBill(id))); }
    public static String format(Bill bill) {
        var transaction = bill.transaction();
        StringBuilder text = new StringBuilder();
        text.append("\nCYBER ACCESS BILL #").append(transaction.id())
                .append("\nCustomer ID: ").append(transaction.userId())
                .append("\nDate: ").append(transaction.createdAt())
                .append("\nStatus: ").append(transaction.status()).append("\n");
        if (bill.items().isEmpty()) text.append("No charges yet.\n");
        for (var item : bill.items()) text.append(String.format("#%d | %s | %s %s x PHP %s = PHP %s%n",
                item.id(), item.description(), item.quantity().toPlainString(), item.unit(),
                item.unitPrice().toPlainString(), item.subtotal().toPlainString()));
        text.append("TOTAL CHARGES: PHP ").append(bill.total().toPlainString()).append("\n");
        if ("Cancelled".equals(transaction.status())) text.append("CANCELLED - retained for history; not collectible.\n");
        text.append("This is a bill, not a payment receipt.\n");
        return text.toString();
    }
}
