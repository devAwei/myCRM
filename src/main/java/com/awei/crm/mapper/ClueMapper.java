package com.awei.crm.mapper;

import com.awei.crm.model.Activity;
import com.awei.crm.model.Clue;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-06 10:49
 **/
public interface ClueMapper {
    /**
    * @Description: 插入一条数据
    * @Param: [clue]
    * @return: int
    * @Author: Awei
    * @Date: 2021/2/6
    */
    int insert(Clue clue);

    List<Map<String, Object>> pageList(Map map);

    int pageListTotal();

    Clue getClueAndNameById(String id);

    int unBund(String id);

    int deleteByPrimaryKey(String clueId);

    Clue selectByPrimaryKey(String id);

}
