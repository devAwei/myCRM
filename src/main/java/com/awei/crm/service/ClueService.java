package com.awei.crm.service;

import com.awei.crm.model.Activity;
import com.awei.crm.model.Clue;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-06 10:53
 **/
public interface ClueService {

    int insert(Clue clue);

    List<Map<String, Object>> pageList(Map<String, Object> map);

    int pageListTotal();

    Clue getClueAndNameById(String id);

}
