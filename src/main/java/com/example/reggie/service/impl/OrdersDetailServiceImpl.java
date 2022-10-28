package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.dao.OrdersDetailMapper;
import com.example.reggie.entity.OrderDetail;
import com.example.reggie.service.OrdersDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrderDetail>implements OrdersDetailService {
}
