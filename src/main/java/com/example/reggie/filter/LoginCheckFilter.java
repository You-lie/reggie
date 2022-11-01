package com.example.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.common.ThreadLocalConfig;
import com.example.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，用来对比通配
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    //过滤器 用来过滤主页访问
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        HttpServletResponse res=(HttpServletResponse) servletResponse;

        //获取URI
        String requestURI=req.getRequestURI();
        //可放行部分
        String urls[]=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg",
                "/doc.html",
                "/webjars/**",
                "/v2/api-docs",
                "/swagger-resources"
        };
        //调用匹配器结果，判断是否放行
        boolean check = check(urls, requestURI);
        //无需处理，true放行

        if (check){
            log.info("登录路径放行: {}",req.getRequestURI());
            filterChain.doFilter(req,res);
            return;
        }
        //登录成功，放行
        if(req.getSession().getAttribute("employee")!=null){
            log.info("成功放行: {}",req.getRequestURI());
            Long empId = (Long) req.getSession().getAttribute("employee");
            //设置线程的用户id属性
            ThreadLocalConfig.setCurrentId(empId);
            filterChain.doFilter(req,res);
            return;
        }
        //手机端登录成功，放行
        if(req.getSession().getAttribute("user")!=null){
            log.info("手机登录成功放行: {}",req.getRequestURI());
            Long userId = (Long) req.getSession().getAttribute("user");
            //设置线程的用户id属性
            ThreadLocalConfig.setCurrentId(userId);
            filterChain.doFilter(req,res);
            return;
        }
        //未登录，拦截
        log.info("未登录拦截");
        res.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));





    }

    /**
     * 路径匹配器，匹配本次是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }


}
