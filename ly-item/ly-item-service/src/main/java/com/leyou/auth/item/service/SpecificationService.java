package com.leyou.auth.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.auth.item.mapper.SpecGroupMapper;
import com.leyou.auth.item.mapper.SpecParamMapper;
import com.leyou.auth.item.pojo.SpecParam;
import com.leyou.auth.item.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {
    @Autowired
    private SpecParamMapper paramMapper;
    @Autowired
    private SpecGroupMapper groupMapper;


    public List<SpecGroup> queryGroupByCid(Long cid) {
        //查询条件
        SpecGroup group=new SpecGroup();
        group.setCid(cid);
        //查询
        List<SpecGroup> list = groupMapper.select(group);
        if (CollectionUtils.isEmpty(list)){
            //没查到
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean seaching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(seaching);
        List<SpecParam> list=paramMapper.select(param);
        if (CollectionUtils.isEmpty(list)){
            //没查到
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public List<SpecGroup> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams = queryParamList(null, cid, null);
        //先把规格参数变成map,map的key是规格组id,map的值是组下的所有参数
        Map<Long,List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParams) {
            if (!map.containsKey(param.getGroupId())){
                //这个组id在Map中不存在，新增一个List
                map.put(param.getGroupId(),new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }
        //填充param到group中
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return specGroups;
    }
}
