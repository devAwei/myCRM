package com.awei.crm.service.impl;

import com.awei.crm.mapper.ActivityRemarkMapper;
import com.awei.crm.model.ActivityRemark;
import com.awei.crm.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-04 21:43
 **/
@Service(value = "activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper arMapper;


    @Override
    public List<ActivityRemark> getRemartById(String id) {
        return arMapper.getRemarkById(id);
    }

    @Override
    public int deleteRemarkById(String id) {
        return arMapper.deleteRemarkById(id);
    }

    @Override
    public int saveRemark(ActivityRemark remark) {
        return arMapper.saveRemark(remark);
    }

    @Override
    public Object getRemarkById(String id) {
        return arMapper.getRemarkByMainId(id);
    }

    @Override
    public int updateRemark(ActivityRemark ar) {
        return arMapper.updateRemark(ar);
    }

}
