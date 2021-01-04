package com.hali.spring.deliveryms.order.utils;

import java.math.BigDecimal;
import java.util.List;

import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.order.domain.Item;

public class OrderUtilities {

    public static BigDecimal countTotalPrice(List<ItemDto> cart){
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < cart.size(); i++){
            total = total.add(cart.get(i).getSubTotal());
        }
        return total;
    }
}