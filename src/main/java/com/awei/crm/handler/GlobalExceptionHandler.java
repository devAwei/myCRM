package com.awei.crm.handler;

import com.awei.crm.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 12:29
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LoginException.class)
    public @ResponseBody
    Object
    logindo(Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", e.getMessage());
        map.put("success", false);
        return map;
    }
}
