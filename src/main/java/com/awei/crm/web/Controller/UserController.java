package com.awei.crm.web.Controller;

import com.awei.crm.exception.LoginException;
import com.awei.crm.mapper.DicTypeMapper;
import com.awei.crm.model.DicType;
import com.awei.crm.model.User;
import com.awei.crm.service.UserService;
import com.awei.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 09:57
 **/
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login.do")
    public @ResponseBody
    Object
    logindo(User user, HttpServletRequest request) throws LoginException {
        String loginPwd = MD5Util.getMD5(user.getLoginPwd());
        String ip = request.getRemoteAddr();
        User u = userService.login(user.getLoginAct(), loginPwd, ip);
        // login 抛出异常，以下方法不会被执行

        //登录成功
        request.getSession().setAttribute("user", u);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @Autowired
    private DicTypeMapper mapper;

    @RequestMapping(value = "/test.do")
    public void tefjkdal() {
        List<String> dicCode = mapper.getDicCode();
        for (String s : dicCode) {
            System.out.println(s);
        }
    }
}

