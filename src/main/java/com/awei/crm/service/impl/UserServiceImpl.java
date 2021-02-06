package com.awei.crm.service.impl;

import com.awei.crm.exception.LoginException;
import com.awei.crm.mapper.UserMapper;
import com.awei.crm.model.User;
import com.awei.crm.service.UserService;
import com.awei.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 09:53
 **/
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userMapper.login(map);
        if (null == user) {
            throw new LoginException("账号密码错误");
        }
        //user 不为空，说明账号密码正确 继续验证 三项信息
        //从user中取出 expireTime lockState ip
        String expireTime = user.getExpireTime();
        String lockState = user.getLockState();
        String uip = user.getAllowIps();
        //判断 有效时间
        if (expireTime.compareTo(DateTimeUtil.getSysTime()) < 0) {
            throw new LoginException("账号已失效");
        }
        //判断ip
        if (!uip.contains(ip)) {
            System.out.println(ip);
            throw new LoginException("ip地址受限");
        }
        //判断锁定状态
        if ("0".equals(lockState)) {
            throw new LoginException("账号已锁定");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

}
