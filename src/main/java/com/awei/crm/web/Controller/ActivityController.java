package com.awei.crm.web.Controller;

import com.awei.crm.model.User;
import com.awei.crm.service.UserService;
import com.awei.crm.utils.DateTimeUtil;
import com.awei.crm.utils.UUIDUtil;
import com.awei.crm.model.Activity;
import com.awei.crm.model.ActivityRemark;
import com.awei.crm.service.ActivityRemarkService;
import com.awei.crm.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-03 16:23
 **/
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService remarkService;

    @RequestMapping(value = "/workbench/activity/getUserList.do")
    public @ResponseBody
    Object getUserList() {
        List<User> uList = userService.getUserList();
        return uList;
    }

    @RequestMapping(value = "/workbench/activity/save.do")
    public @ResponseBody
    Object saveDo(Activity ac, HttpServletRequest request) {
        String id = UUIDUtil.getUUID();
        ac.setId(id);
        //创建时间
        String createTime = DateTimeUtil.getSysTime();
        ac.setCreateTime(createTime);
        String name = ((User) request.getSession().getAttribute("user")).getName();
        ac.setCreateBy(name);
        int result = activityService.save(ac);
        Map<String, Object> map = new HashMap<>();
        if (result > 0) {
            map.put("success", true);
            return map;
        }
        map.put("success", false);
        return map;
    }

    //刷新市场活动
    @RequestMapping(value = "/workbench/activity/pageList.do")
    public @ResponseBody
    Object pageList(Activity ac, Integer pageNo, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("name", ac.getName());
        map.put("owner", ac.getOwner());
        map.put("startDate", ac.getStartDate());
        map.put("endDate", ac.getEndDate());

        List<Map<String, Object>> alist = activityService.pageList(map);
        int total = activityService.pageListTotal(map);
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("total", total);
        // 再往 reMap 塞一个list<map>
        reMap.put("dataList", alist);
        return reMap;
    }

    //删除 市场活动
    @PostMapping(value = "/workbench/activity/delete.do")
    public @ResponseBody
    Object deleteDo(String[] id) {
        int i = activityService.deleteSelective(id);
        Map<String, Object> remap = new HashMap<>();
        if (i > 0) {
            remap.put("success", true);
        } else remap.put("success", false);
        return remap;
    }


    //修改 市场活动
    @PostMapping(value = "/workbench/activity/getUserListAndActivity.do")
    public @ResponseBody
    Object getUserListAndActivityDo(String id) {
        Map<String, Object> reMap = new HashMap<>();
        //查 userList
        List<User> userList = userService.getUserList();
        reMap.put("userList", userList);
        //根据id 查Activity 对象
        Activity ac = activityService.getActivityByid(id);
        reMap.put("activity", ac);
        return reMap;
    }


    @RequestMapping(value = "/workbench/activity/update.do")
    public @ResponseBody
    Object updateDo(Activity ac, HttpServletRequest request) {
        ac.setEditTime(DateTimeUtil.getSysTime());
        ac.setEditBy(((User) (request.getSession().getAttribute("user"))).getName());
        Map<String, Object> map = new HashMap<>();
        int result = activityService.update(ac);
        if (result > 0) {
            map.put("success", true);
        } else map.put("success", false);
        return map;
    }


    //跳转 市场活动详情页面
    @RequestMapping(value = "/workbench/activity/detail.do")
    public String detailDO(String id, HttpServletRequest request) {
        Activity ac = activityService.getAcvAndName(id);
        request.setAttribute("ac", ac);
        return "workbench/activity/detail";
    }


}
