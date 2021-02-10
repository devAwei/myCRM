package com.awei.crm.web.Controller;

import com.awei.crm.exception.NullCustomerNameList;
import com.awei.crm.service.CustomerService;
import com.awei.crm.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-10 10:43
 **/
@Controller
public class TranController {

    @Autowired
    private CustomerService customerService;


    /********************************************************/
    @RequestMapping("/workbench/transaction/getCustomerName.do")
    public @ResponseBody
    Object getCustomerName(String name) throws NullCustomerNameList {
        List<String> userNameList = customerService.getCustomerNameList(name);
        return userNameList;
    }
}
