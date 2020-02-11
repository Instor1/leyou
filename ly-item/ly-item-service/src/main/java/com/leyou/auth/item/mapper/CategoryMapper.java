package com.leyou.auth.item.mapper;

import com.leyou.auth.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;


public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
}
