package com.example.reggie.controller;

import com.example.reggie.common.R;
import com.example.reggie.entity.Orders;
import com.example.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("提交订单数据:{}",orders);
        ordersService.submit(orders);
        return R.success("提交订单成功");
    }
}
