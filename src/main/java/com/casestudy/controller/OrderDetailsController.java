package com.casestudy.controller;

import com.casestudy.service.orderDetails.IOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderDetailsController {
    @Autowired
    private IOrderDetailsService orderDetailsService;

    @GetMapping("/save-order")
    public void saveOrder() {
    }


}
