package com.joysistvi.CyberAccess.model;

import java.math.BigDecimal;

// Read-only projection: Rates.java remains owned by the rates module.
// Holds a saved rate and the service it belongs to.
public record ServiceRate (int id, int serviceId, String serviceName, String rateName,
                           BigDecimal price, String unit) {}
