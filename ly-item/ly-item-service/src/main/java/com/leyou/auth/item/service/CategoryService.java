package com.leyou.auth.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.auth.item.mapper.CategoryMapper;
import com.leyou.auth.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOuND);
        }
        return list;
    }

    public List<Category> queryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOuND);
        }
        return list;
    }


    public List<Category> queryAllByCid3(Long id) {
            Category c3 = this.categoryMapper.selectByPrimaryKey(id);
            Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
            Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
            return Arrays.asList(c1,c2,c3);
    }

}
