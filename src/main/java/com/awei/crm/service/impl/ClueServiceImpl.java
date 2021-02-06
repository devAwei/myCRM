package com.awei.crm.service.impl;

import com.awei.crm.mapper.ClueMapper;
import com.awei.crm.model.Activity;
import com.awei.crm.model.Clue;
import com.awei.crm.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-06 10:53
 **/
@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;
    @Override
    public int insert(Clue clue) {
        return clueMapper.insert(clue);
    }

    @Override
    public List<Map<String, Object>> pageList(Map<String, Object> map) {

        return clueMapper.pageList(map);
    }

    @Override
    public int pageListTotal() {
        return clueMapper.pageListTotal();
    }

    @Override
    public Clue getClueAndNameById(String id) {
        return clueMapper.getClueAndNameById(id);
    }


}
