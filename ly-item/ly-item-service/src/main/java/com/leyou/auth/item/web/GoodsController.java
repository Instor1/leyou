package com.leyou.auth.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.auth.item.pojo.Sku;
import com.leyou.auth.item.pojo.Spu;
import com.leyou.auth.item.pojo.SpuDetail;
import com.leyou.auth.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "key",required = false) String key
    ){
        return ResponseEntity.ok(goodsService.querySpuByPage(page,rows,saleable,key));

    }

    /**
     * 商品新增
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }/**
     * 商品修改
     * @param spu
     * @return
     */

    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据spu的id查询想去detail
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.queryDetailById(id));
    }

    /**
     * 根据spu查询下面的所有sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuById(@RequestParam("id") Long id){
        return ResponseEntity.ok(goodsService.querySkuBySpuId(id));
    }
}
