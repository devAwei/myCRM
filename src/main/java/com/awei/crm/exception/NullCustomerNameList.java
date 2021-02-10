package com.awei.crm.exception;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-10 10:59
 **/
public class NullCustomerNameList extends Exception {
    public NullCustomerNameList() {
        super();

    }

    public NullCustomerNameList(String message) {
        super(message);
    }
}
