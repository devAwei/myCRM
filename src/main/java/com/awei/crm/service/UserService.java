package com.awei.crm.service;

import com.awei.crm.exception.LoginException;
import com.awei.crm.model.User;

import java.util.List;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-05 14:35
 **/
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
