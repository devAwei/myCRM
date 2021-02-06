package com.awei.crm.service;

import com.awei.crm.model.DicType;
import com.awei.crm.model.DicValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-05 15:50
 **/
public interface DicService {

    Map<String, List<DicValue>> getAll();


}
