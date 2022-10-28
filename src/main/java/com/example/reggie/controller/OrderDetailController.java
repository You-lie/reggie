package com.example.reggie.controller;

import com.example.reggie.service.OrdersDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/orderDetail")
@RestController
public class OrderDetailController {
    private OrdersDetailService ordersDetailService;
}
