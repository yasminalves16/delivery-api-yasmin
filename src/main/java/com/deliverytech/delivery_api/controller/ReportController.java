package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.TotalSalesByRestaurantDTO;
import com.deliverytech.delivery_api.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService service;

    @GetMapping("/total-sales-by-restaurant")
    public List<TotalSalesByRestaurantDTO> totalSalesByRestaurant(){
        return service.totalSalesByRestaurant();
    }

    @GetMapping("/customer-ranking")
    public List<Object[]> customerRanking(){
        return service.customerRanking();
    }
}
