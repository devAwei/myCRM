package com.awei.crm.service;

import com.awei.crm.model.ActivityRemark;

import java.util.List;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-04 21:43
 **/
public interface ActivityRemarkService {

    List<ActivityRemark> getRemartById(String id);

    int deleteRemarkById(String id);

    int saveRemark(ActivityRemark remark);

    Object getRemarkById(String id);

    int updateRemark(ActivityRemark ar);
}
