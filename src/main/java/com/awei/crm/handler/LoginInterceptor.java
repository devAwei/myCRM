package com.awei.crm.handler;

import com.awei.crm.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 13:31
 **/
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*if ("/login.jsp".equals(request.getRequestURI())) {
            System.out.println(request.getRequestURI());
            return true;
        }*/
        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            return true;
        }
        System.out.println(request.getRequestURI());

        System.out.println("intercepter success!");
        String contextPath = request.getContextPath();
        System.out.println(contextPath);
        response.sendRedirect(contextPath + "/login.jsp");
        return false;
    }

}
