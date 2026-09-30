package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.service.BillServiceImpl;
import com.joysistvi.CyberAccess.model.Bill;
import java.sql.SQLException;

// Connects the bill display to its service logic
public class BillController {
    private final BillServiceImpl service;
    public BillController(BillServiceImpl service) { this.service = service; }
    public Bill getBill(int id) throws SQLException { return service.getBill(id); }
}
