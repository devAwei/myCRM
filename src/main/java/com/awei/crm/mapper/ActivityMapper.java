package com.awei.crm.mapper;

import com.awei.crm.model.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    int deleteByPrimaryKey(String id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);


    int save(Activity ac);

    List<Map<String, Object>> pageList(Map<String, Object> map);

    int pageListTotal(Map<String, Object> map);

    int deleteSelective(String[] strs);

    Activity getActivityByid(String id);

    int update(Activity ac);

    Activity getActAndName(String id);

}