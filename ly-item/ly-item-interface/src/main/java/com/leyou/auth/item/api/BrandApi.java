package com.leyou.auth.item.api;

import com.leyou.auth.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandApi {
    @GetMapping("brand/{id}")
    Brand queryBrandId(@PathVariable("id") Long id);

    @GetMapping("brand/list")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
