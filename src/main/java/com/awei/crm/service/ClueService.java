package com.awei.crm.service;

import com.awei.crm.exception.CULDException;
import com.awei.crm.model.Activity;
import com.awei.crm.model.Clue;
import com.awei.crm.model.Tran;
import com.awei.crm.model.User;

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

    int unBund(String id);

    int deleteByPrimaryKey(String clueId);

    boolean convert(Tran tran, User user,String id) throws CULDException;

}
