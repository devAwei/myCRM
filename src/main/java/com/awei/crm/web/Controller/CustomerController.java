package com.awei.crm.web.Controller;

import com.awei.crm.exception.CULDException;
import com.awei.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-08 13:46
 **/
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @GetMapping("/workbench/customer/pageList.do")
    public @ResponseBody
    Object getPageListDo(Integer pageNo, Integer pageSize) throws CULDException {
        Map<String, Object> reMap = customerService.pageList(pageNo,pageSize);
        return reMap;
    }
}
