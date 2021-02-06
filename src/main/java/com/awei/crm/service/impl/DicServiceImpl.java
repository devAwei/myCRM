package com.awei.crm.service.impl;

import com.awei.crm.mapper.DicTypeMapper;
import com.awei.crm.mapper.DicValueMapper;
import com.awei.crm.model.DicType;
import com.awei.crm.model.DicValue;
import com.awei.crm.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-05 15:51
 **/
@Service("dicService")
public class DicServiceImpl implements DicService {
    @Autowired
    private DicTypeMapper tMapper;
    @Autowired
    private DicValueMapper vMapper;

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();

        //1.获取所有的DicType，就是有哪些类型
        List<DicType> types = tMapper.getTypes();

        //2.获取上面每种类型对应的Value值，遍历List集合
        for (DicType code : types){

            List<DicValue> dicValueList = vMapper.getValuesByTypeCode(code.getCode());
            map.put(code.getCode(),dicValueList);
        }

        return map;
    }

}
