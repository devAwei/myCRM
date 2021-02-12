package com.awei.crm.service;

import com.awei.crm.exception.CULDException;
import com.awei.crm.model.Tran;
import com.awei.crm.model.User;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-07 12:45
 **/
public interface TranService {


    boolean saveTran(Tran tran, User user) throws CULDException;
}
