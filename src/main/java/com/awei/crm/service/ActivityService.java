package com.awei.crm.service;

import com.awei.crm.model.Activity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 16:23
 **/
public interface ActivityService {
    int save(Activity ac);

    List<Map<String, Object>> pageList(Map<String, Object> map);

    int pageListTotal(Map<String, Object> map);

    int deleteSelective(String[] id);

    Activity getActivityByid(String id);

    int update(Activity ac);

    Activity getAcvAndName(String id);

    List<Activity> showActivityList(String id);

}
