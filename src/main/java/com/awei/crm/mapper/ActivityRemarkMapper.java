package com.awei.crm.mapper;

import com.awei.crm.model.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActivityRemark record);

    int insertSelective(ActivityRemark record);

    ActivityRemark selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActivityRemark record);

    int updateByPrimaryKey(ActivityRemark record);

    List<ActivityRemark> getRemarkById(String id);

    int deleteRemarkById(String id);

    int saveRemark(ActivityRemark remark);

    Object getRemarkByMainId(String id);

    int updateRemark(ActivityRemark ar);
}