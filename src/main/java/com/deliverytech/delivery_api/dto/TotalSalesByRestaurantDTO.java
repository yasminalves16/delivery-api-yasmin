package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public class TotalSalesByRestaurantDTO {
    private String restaurant;
    private BigDecimal totalSales;
    
    public TotalSalesByRestaurantDTO(String restaurant, BigDecimal totalSales) {
        this.restaurant = restaurant;
        this.totalSales = totalSales;
    }

    public String getRestaurant(){
        return restaurant;
    }

    public BigDecimal getTotalSales(){
        return totalSales;
    }

}
