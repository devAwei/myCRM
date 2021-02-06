package com.awei.crm.web.Controller;

import com.awei.crm.model.Activity;
import com.awei.crm.model.Clue;
import com.awei.crm.model.User;
import com.awei.crm.service.ClueService;
import com.awei.crm.service.UserService;
import com.awei.crm.utils.DateTimeUtil;
import com.awei.crm.utils.UUIDUtil;
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
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-06 09:05
 **/
@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClueService clueService;

    @RequestMapping("/workbench/clue/getUserList.do")
    public @ResponseBody
    Object getUserListDo() {
        List<User> userList = userService.getUserList();

        return userList;
    }

    @RequestMapping("/workbench/clue/pageList.do")
    public @ResponseBody
    Object pageListDo(Integer pageNo, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);


        List<Map<String, Object>> alist = clueService.pageList(map);
        int total = clueService.pageListTotal();
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("total", total);
        // 再往 reMap 塞一个list<map>
        reMap.put("dataList", alist);
        return reMap;
    }

    @PostMapping("/workbench/clue/save.do")
    public @ResponseBody
    Object saveDo(HttpServletRequest request, Clue clue) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        System.out.println("=======================\n" + clue);
        Map<String, Object> map = new HashMap<>();
        int num = clueService.insert(clue);
        if (num > 0) {
            map.put("success", true);
        } else map.put("success", false);
        return map;
    }

    @RequestMapping("/workbench/clue/detail.do")
    public String detailDo(String id, HttpServletRequest request) {
        Clue clue = clueService.getClueAndNameById(id);
        request.setAttribute("clue", clue);
        return "workbench/clue/detail";
    }

    /**
    * @Description:
    * @Param: [id]  传过来的 客户的  id  刘冬冬
    * @return: java.lang.Object
    * @Author: Awei
    * @Date: 2021/2/6
    */
    @RequestMapping("/workbench/clue/showActivityList.do")
    public @ResponseBody
    Object showActivityList(String id) {
        //刷新市场活动关联表
        List<Activity> aclist = clueService.showActivityList(id);
        return aclist;
    }
}
