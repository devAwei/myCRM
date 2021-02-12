package com.awei.crm.service.impl;

import com.awei.crm.exception.CULDException;
import com.awei.crm.mapper.TranMapper;
import com.awei.crm.model.Tran;
import com.awei.crm.model.User;
import com.awei.crm.service.TranService;
import com.awei.crm.utils.DateTimeUtil;
import com.awei.crm.utils.UUIDUtil;
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


    @Override
    public boolean saveTran(Tran tran, User user) throws CULDException {
       // 表单提交字段 owner money name expectedDate customerId stage type source activityId contactsId description contactSummary nextContactTime
       //表缺少的字段 id  createBy createTime  editBy editTime
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        int insertNum = tranMapper.insert(tran);
        if (insertNum == 0) {
            throw new CULDException("添加交易失败,交易名称：" + tran.getName());
        }

        return true;
    }
}
