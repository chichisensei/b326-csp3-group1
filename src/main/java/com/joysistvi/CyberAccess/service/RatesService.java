package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Rates;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface RatesService {

    List<Rates> getAll() throws SQLException;

    List<Rates> getActive() throws SQLException;

    List<Rates> getByType(String rateType) throws SQLException;

    Rates getById(int id) throws SQLException;

    Rates add(int serviceId, String rateName, String rateType, BigDecimal price, String unit) throws SQLException;

    void update(int id, int serviceId, String rateName, String rateType, BigDecimal price, String unit,
                String status) throws SQLException;

    void changePrice(int id, BigDecimal price) throws SQLException;

    void changeStatus(int id, String status) throws SQLException;

    void delete(int id) throws SQLException;
}
