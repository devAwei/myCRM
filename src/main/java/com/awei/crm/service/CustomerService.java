package com.awei.crm.service;

import com.awei.crm.exception.CULDException;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-08 13:48
 **/
public interface CustomerService {


    Map<String, Object> pageList(Integer pageNo,Integer pageSize) throws CULDException;

}
