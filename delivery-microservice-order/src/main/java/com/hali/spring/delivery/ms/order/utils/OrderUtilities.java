package com.hali.spring.delivery.ms.order.utils;

import java.math.BigDecimal;
import java.util.List;

import com.hali.spring.delivery.microservice.order.domain.Item;
import com.hali.spring.delivery.ms.model.ItemDto;

public class OrderUtilities {

    public static BigDecimal countTotalPrice(List<ItemDto> cart){
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < cart.size(); i++){
            total = total.add(cart.get(i).getSubTotal());
        }
        return total;
    }
}