package com.awei.crm.listener;

import com.awei.crm.mapper.DicTypeMapper;
import com.awei.crm.mapper.DicValueMapper;
import com.awei.crm.model.DicType;
import com.awei.crm.model.DicValue;
import com.awei.crm.service.DicService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-05 16:41
 **/
public class SysInitListener implements   ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        //拿到全局作用域对象
        ServletContext application = event.getServletContext();
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("conf/applicationContext.xml");
        DicService dicService = (DicService) wac.getBean("dicService");
        Map<String, List<DicValue>> map = dicService.getAll();
        //遍历map
        Set<String> keySet = map.keySet();
        for (String key : keySet){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("=====监听器执行，数据字典初始化结束====");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
