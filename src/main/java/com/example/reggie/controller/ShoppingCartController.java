package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.common.R;
import com.example.reggie.common.ThreadLocalConfig;
import com.example.reggie.entity.ShoppingCart;
import com.example.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("添加购物车：{}",shoppingCart);
        //获取并设置用户id,向数据库发送请求来进行查询是哪个购物车数据
        Long currentId = ThreadLocalConfig.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询本次添加的菜品是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper <ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if (dishId!=null){
            //根据前端传过来的shoppingCart的id数据来判断本次添加的是套餐还是菜品
            //本次添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //本次添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if (one!=null){
            //说明不是第一次添加这个菜品 应该直接在数量上加一
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            //第一次添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }
        return R.success(one);
    }

    /**
     * 减少购物车菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        //获取并设置用户id,向数据库发送请求来进行查询是哪个购物车数据
        Long currentId = ThreadLocalConfig.getCurrentId();
        //查询本次添加的菜品是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper <ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if (dishId!=null){
            //根据前端传过来的shoppingCart的id数据来判断本次添加的是套餐还是菜品
            //本次添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //本次添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if (one!=null){
            //说明不是第一次添加这个菜品 应该直接在数量上加一
            Integer number = one.getNumber();
            if (number==1){
                shoppingCartService.removeById(one);
                return R.success(one);
            }
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }
        return R.success(one);
    }
    @DeleteMapping("/clean")
    public R<String>delete(){
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,ThreadLocalConfig.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("展示购物车~");
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,ThreadLocalConfig.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
}
