package com.awei.crm.mapper;

import com.awei.crm.model.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(String id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    Customer selectByCompany(String company);

    int countCustomer();

    List<Customer> selectCustomer(@Param("pageNo")Integer pageNo,@Param("pageSize") Integer pageSize);

}