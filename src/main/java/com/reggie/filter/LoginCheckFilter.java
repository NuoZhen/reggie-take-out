package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 检查用户是否已经登录
 */
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI = httpServletRequest.getRequestURI();

        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/**"
        };

        //判断是否需要路径处理
        boolean check = check(urls, requestURI);

        //不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //判断登录状态，如果已登录，则直接放行
        if(httpServletRequest.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("employee"));

            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        //判断登录状态，如果已登录，则直接放行
        if(httpServletRequest.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("user"));

            Long userId = (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        //未登录
        log.info("用户未登录");
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
