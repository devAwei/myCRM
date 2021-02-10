package com.awei.crm.service.impl;

import com.awei.crm.exception.CULDException;
import com.awei.crm.exception.NullCustomerNameList;
import com.awei.crm.mapper.CustomerMapper;
import com.awei.crm.model.Customer;
import com.awei.crm.service.CustomerService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-08 13:48
 **/
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public Map<String, Object> pageList(Integer pageNo, Integer pageSize) throws CULDException {
        //return Map{"total":total,"dataList":[{customer1},{customer2},{customer2}.....]}
        Map<String, Object> reMap = new HashMap<>();
        //查customer 总数
        int total = 0;
        total = customerMapper.countCustomer();
        reMap.put("total", total);
        if (total == 0) {
            throw new CULDException("客户表无记录");
        }
        Map<String, Integer> paraMap = new HashMap<>();

        List<Customer> ctmList = customerMapper.selectCustomer( (pageNo-1)*pageSize, pageSize);
        reMap.put("dataList", ctmList);
        return reMap;
    }

    //////////////////////////////////////////////////////////////////////
    @Override
    public List<String> getCustomerNameList(String name) throws NullCustomerNameList {
        List<String> nameList = customerMapper.getCustomerNameList(name);
        if (null == nameList) {
            throw new NullCustomerNameList("没有客户");
        }

        return nameList;
    }
}
