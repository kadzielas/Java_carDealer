package com.car.dealer.model;

import com.car.dealer.common.Currency;
import com.car.dealer.common.Fuel;
import com.car.dealer.common.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Car implements Serializable {
    private Integer ID;
    private Manufacturer manufacturer;
    private String model;
    private BigDecimal engine;
    private Fuel fuel;
    private Integer year;
    private BigDecimal price;
    private Currency currency;
}
