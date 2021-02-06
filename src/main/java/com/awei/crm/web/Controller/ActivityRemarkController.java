package com.awei.crm.web.Controller;

import com.awei.crm.model.User;
import com.awei.crm.utils.DateTimeUtil;
import com.awei.crm.utils.UUIDUtil;
import com.awei.crm.model.ActivityRemark;
import com.awei.crm.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM
 * @author: Awei
 * @create: 2021-02-05 08:46
 **/
@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService remarkService;

    //备注刷新 ajax
    @RequestMapping(value = "/workbench/activity/getRemarkList.do")
    public @ResponseBody
    Object getRemarkListDo(String id) {
        // 根据id 查备注信息
        List<ActivityRemark> arList = remarkService.getRemartById(id);
        return arList;
    }

    @RequestMapping(value = "/workbench/activity/deleteRemark.do")
    public @ResponseBody
    Object deleteRemarkDo(String id) {
        Map<String, Object> reMap = new HashMap<>();
        int result = remarkService.deleteRemarkById(id);
        if (result > 0) {
            reMap.put("success", true);
        } else reMap.put("success", false);
        return reMap;
    }


    @PostMapping(value = "/workbench/activity/saveRemark.do")
    public @ResponseBody
    Object saveRemarkDo(ActivityRemark remark, HttpServletRequest request) {
        String CreateBy = ((User) request.getSession().getAttribute("user")).getName();
        Map<String, Object> reMap = new HashMap<>();
        String uuid = UUIDUtil.getUUID();
        System.out.println(uuid);
        remark.setId(uuid);
        remark.setCreateBy(CreateBy);
        remark.setCreateTime(DateTimeUtil.getSysTime());
        remark.setEditFlag("0");
        int result = remarkService.saveRemark(remark);
        ActivityRemark queryAr = (ActivityRemark) remarkService.getRemarkById(remark.getId());
        if (result > 0) {
            reMap.put("success", true);
            reMap.put("ar", queryAr);
        } else reMap.put("success", false);
        return reMap;
    }


    //更新备注
    @PostMapping(value = "/workbench/activity/updateRemark.do")
    public @ResponseBody
    Object updateRemarkDo(ActivityRemark ar, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        ar.setEditFlag("1");
        ar.setEditBy(user.getName());
        ar.setEditTime(DateTimeUtil.getSysTime());
        int num = remarkService.updateRemark(ar);
        if (num > 0) {
            map.put("success", true);
            ActivityRemark returnRemark = (ActivityRemark) remarkService.getRemarkById(ar.getId());
            map.put("ar", returnRemark);
        } else map.put("success", false);

        return map;
    }
}
