package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Rates;
import com.joysistvi.CyberAccess.repo.RatesRepo;
import com.joysistvi.CyberAccess.repo.RatesRepoImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class RatesServiceImpl implements RatesService {

    private static final int NAME_MAX = 50;
    private static final int TYPE_MAX = 30;
    private static final int UNIT_MAX = 20;

    private final RatesRepo repo;

    public RatesServiceImpl() {
        this(new RatesRepoImpl());
    }

    public RatesServiceImpl(RatesRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Rates> getAll() throws SQLException {
        return repo.findAll();
    }

    @Override
    public List<Rates> getActive() throws SQLException {
        return repo.findActive();
    }

    @Override
    public List<Rates> getByType(String rateType) throws SQLException {
        return repo.findByType(validText(rateType, "Rate type", TYPE_MAX));
    }

    @Override
    public Rates getById(int id) throws SQLException {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No rate found with ID " + id + "."));
    }

    @Override
    public Rates add(int serviceId, String rateName, String rateType, BigDecimal price, String unit)
            throws SQLException {
        String name = validText(rateName, "Rate name", NAME_MAX);
        String type = validText(rateType, "Rate type", TYPE_MAX);
        String un = validText(unit, "Unit", UNIT_MAX);
        BigDecimal pr = validPrice(price);

        if (repo.existsByName(name)) {
            throw new IllegalArgumentException("A rate named \"" + name + "\" already exists.");
        }

        Rates rate = new Rates(serviceId, name, type, pr, un, Rates.STATUS_ACTIVE);
        repo.save(rate);
        return rate;
    }

    @Override
    public void update(int id, int serviceId, String rateName, String rateType, BigDecimal price, String unit,
                       String status) throws SQLException {
        Rates current = getById(id);
        String name = validText(rateName, "Rate name", NAME_MAX);
        String type = validText(rateType, "Rate type", TYPE_MAX);
        String un = validText(unit, "Unit", UNIT_MAX);
        BigDecimal pr = validPrice(price);
        String st = validStatus(status);

        if (!name.equalsIgnoreCase(current.getRateName()) && repo.existsByName(name)) {
            throw new IllegalArgumentException("A rate named \"" + name + "\" already exists.");
        }

        current.setServiceId(serviceId);
        current.setRateName(name);
        current.setRateType(type);
        current.setPrice(pr);
        current.setUnit(un);
        current.setStatus(st);
        repo.update(current);
    }

    @Override
    public void changePrice(int id, BigDecimal price) throws SQLException {
        Rates current = getById(id);
        current.setPrice(validPrice(price));
        repo.update(current);
    }

    @Override
    public void changeStatus(int id, String status) throws SQLException {
        String st = validStatus(status);
        getById(id); // makes sure it exists
        repo.updateStatus(id, st);
    }

    @Override
    public void delete(int id) throws SQLException {
        getById(id);
        // Rates already used by sessions are protected by a foreign key and will
        // raise an SQLException. Deactivating the rate is the alternative.
        repo.delete(id);
    }

    private String validText(String value, String label, int max) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be empty.");
        }
        String trimmed = value.trim();
        if (trimmed.length() > max) {
            throw new IllegalArgumentException(label + " must be at most " + max + " characters.");
        }
        return trimmed;
    }

    private BigDecimal validPrice(BigDecimal price) {
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        BigDecimal rounded = price.setScale(2, RoundingMode.HALF_UP);
        // decimal(10,2) holds at most 99,999,999.99
        if (rounded.compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("Price is too large.");
        }
        return rounded;
    }

    private String validStatus(String status) {
        if (status != null) {
            if (Rates.STATUS_ACTIVE.equalsIgnoreCase(status.trim())) {
                return Rates.STATUS_ACTIVE;
            }
            if (Rates.STATUS_INACTIVE.equalsIgnoreCase(status.trim())) {
                return Rates.STATUS_INACTIVE;
            }
        }
        throw new IllegalArgumentException("Status must be Active or Inactive.");
    }
}
