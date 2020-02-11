package com.leyou.auth.service;

import com.leyou.auth.item.pojo.*;
import com.leyou.item.pojo.*;
import com.leyou.auth.page.client.BrandClient;
import com.leyou.auth.page.client.CategoryClient;
import com.leyou.auth.page.client.GoodsClient;
import com.leyou.auth.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class PageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private TemplateEngine templateEngine;
    public Map<String, Object> loadModel(Long spuId) {
        Map<String,Object> model=new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = spu.getSkus();
        //查询详情
        SpuDetail detail = spu.getSpuDetail();
        //查询brand
        Brand brand = brandClient.queryBrandId(spu.getBrandId());
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询specs
        List<SpecGroup> specs = specClient.queryGroupByCid(spu.getCid3());
        model.put("spu",spu);
        model.put("skus",skus);
        model.put("detail",detail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("specs",specs);
        return model;
    }

    public void createHtml(Long spuId){
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        File file = new File("/test", spuId + ".html");

        if (file.exists()){
            file.delete();
        }

        try(PrintWriter writer = new PrintWriter(file,"UTF-8")){
            //生成html
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            log.error("[静态页服务]生成静态页异常！",e);
        }

    }

    public void deleteHtml(Long spuId) {
        File file = new File("/test", spuId + ".html");
        if (file.exists()){
            file.delete();
        }
    }
}
