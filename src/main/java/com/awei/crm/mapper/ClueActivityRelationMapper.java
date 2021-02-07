package com.awei.crm.mapper;

import com.awei.crm.model.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(ClueActivityRelation record);

    int insertSelective(ClueActivityRelation record);

    ClueActivityRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ClueActivityRelation record);

    int updateByPrimaryKey(ClueActivityRelation record);

    int updateRelation(String cId, String id);

    int deleteByClueId(String id);


    List<ClueActivityRelation> selectByClueId(String clueId);

}