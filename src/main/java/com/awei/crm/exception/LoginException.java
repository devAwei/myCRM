package com.awei.crm.exception;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 12:21
 **/
public class LoginException extends UserException {
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
