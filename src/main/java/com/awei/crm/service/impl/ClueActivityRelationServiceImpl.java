package com.awei.crm.service.impl;

import com.awei.crm.mapper.ClueActivityRelationMapper;
import com.awei.crm.model.ClueActivityRelation;
import com.awei.crm.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-06 20:53
 **/
@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {

    @Autowired
    private ClueActivityRelationMapper carMapper;
    @Override
    public int updateRelation(ClueActivityRelation car) {
        return carMapper.insert(car);
    }
}
