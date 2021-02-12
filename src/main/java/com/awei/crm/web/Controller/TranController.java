package com.awei.crm.web.Controller;

import com.awei.crm.exception.CULDException;
import com.awei.crm.exception.NullCustomerNameList;
import com.awei.crm.model.Tran;
import com.awei.crm.model.User;
import com.awei.crm.service.CustomerService;
import com.awei.crm.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

    @Autowired
    private TranService tranService;
    /********************************************************/
    @RequestMapping("/workbench/transaction/getCustomerName.do")
    public @ResponseBody
    Object getCustomerName(String name) throws NullCustomerNameList {
        List<String> userNameList = customerService.getCustomerNameList(name);
        return userNameList;
    }

    @PostMapping("/workbench/transaction/save.do")
    public @ResponseBody
    Object saveTran(Tran tran, HttpServletRequest request) throws CULDException {

        User loginUser = (User) request.getSession().getAttribute("user");
        boolean result = tranService.saveTran(tran,loginUser);
        Map<String, Object> retMap = new HashMap<>();
        if (result) {
            retMap.put("success", true);
        } else retMap.put("success", false);
        return retMap;
    }
}
