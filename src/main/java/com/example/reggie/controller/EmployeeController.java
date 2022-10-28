package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.entity.Employee;
import com.example.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    //用户登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //md5加密
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //查询用户名 mybatis plus
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp==null){
            return R.error("用户不存在");
        }
        //查询密码
        if (!emp.getPassword().equals(password)){
            return R.error("用户密码错误");
        }
        //查询用户状态
        if (emp.getStatus()==0){
            return R.error("用户已被禁用");
        }
        //登陆成功
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }
    //用户退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    //添加员工
    @PostMapping
    public R<String> save( HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息: {}",employee.toString());
        //用户初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //是谁添加的员工
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("添加成功");
    }
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page: {},pageSize: {},name: {}",page,pageSize,name);
        Page pageInfo= new Page(page,pageSize);
        //查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);//name不为空
        wrapper.orderByDesc(Employee::getUpdateTime);//排序
        //执行
        employeeService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    //更新用户
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateUser(empId);//更新人
        //employee.setUpdateTime(LocalDateTime.now());//更新时间
        employeeService.updateById(employee);//按照id更新
        return R.success("更新成功");
        //json最多支持16位 但是这个id是18位 需要把json转为java对象进行序列化  序列化使用jackson
    }

    /**
     * 获取编辑里的员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查员工信息");
        Employee byId = employeeService.getById(id);
         if(byId==null){
             return  R.error("没有查询到员工信息");
         }
        return R.success( byId);
    }

}
