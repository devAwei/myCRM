package com.awei.crm.service.impl;

import com.awei.crm.mapper.TranMapper;
import com.awei.crm.model.Tran;
import com.awei.crm.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-07 12:45
 **/
@Service

public class TranServiceImpl implements TranService {
    @Autowired
    private TranMapper tranMapper;
}
