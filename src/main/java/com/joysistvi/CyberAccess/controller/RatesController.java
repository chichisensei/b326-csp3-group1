package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.Rates;
import com.joysistvi.CyberAccess.service.RatesService;
import com.joysistvi.CyberAccess.service.RatesServiceImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class RatesController {

    private final RatesService service;

    public RatesController() {
        this(new RatesServiceImpl());
    }

    public RatesController(RatesService service) {
        this.service = service;
    }

    public List<Rates> getAll() throws SQLException {
        return service.getAll();
    }

    public List<Rates> getActive() throws SQLException {
        return service.getActive();
    }

    public List<Rates> getByType(String rateType) throws SQLException {
        return service.getByType(rateType);
    }

    public Rates getById(int id) throws SQLException {
        return service.getById(id);
    }

    public Rates add(int serviceId, String rateName, String rateType, BigDecimal price, String unit)
            throws SQLException {
        return service.add(serviceId, rateName, rateType, price, unit);
    }

    public void update(int id, int serviceId, String rateName, String rateType, BigDecimal price, String unit,
                       String status) throws SQLException {
        service.update(id, serviceId, rateName, rateType, price, unit, status);
    }

    public void changePrice(int id, BigDecimal price) throws SQLException {
        service.changePrice(id, price);
    }

    public void changeStatus(int id, String status) throws SQLException {
        service.changeStatus(id, status);
    }

    public void delete(int id) throws SQLException {
        service.delete(id);
    }
}
