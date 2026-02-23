package com.deliverytech.delivery_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.TotalSalesByRestaurantDTO;
import com.deliverytech.delivery_api.repository.CustomerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final CustomerOrderRepository repository;
    
    public List<TotalSalesByRestaurantDTO> totalSalesByRestaurant(){
        return repository.totalSalesByRestaurant();
    }  

    public List<Object[]> customerRanking(){
        return repository.customerRanking();
    }

}
