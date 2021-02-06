package com.awei.crm.service.impl;

import com.awei.crm.mapper.ActivityMapper;
import com.awei.crm.model.Activity;
import com.awei.crm.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 16:23
 **/
@Service(value = "activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override

    public int save(Activity ac) {
        return activityMapper.save(ac);
    }

    @Override
    public List<Map<String, Object>> pageList(Map<String, Object> map) {
        return activityMapper.pageList(map);
    }

    @Override
    public int pageListTotal(Map<String, Object> map) {
        return activityMapper.pageListTotal(map);
    }

    @Override
    public int deleteSelective(String[] id) {
        return activityMapper.deleteSelective(id);
    }

    @Override
    public Activity getActivityByid(String id) {
        return activityMapper.getActivityByid(id);
    }

    @Override
    public int update(Activity ac) {
        return activityMapper.update(ac);
    }

    @Override
    public Activity getAcvAndName(String id) {
        return activityMapper.getActAndName(id);
    }

    @Override
    public List<Activity> showActivityList(String id) {
        return activityMapper.showActivityList(id);
    }

}
