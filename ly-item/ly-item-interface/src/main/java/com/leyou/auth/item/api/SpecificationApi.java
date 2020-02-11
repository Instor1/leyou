package com.leyou.auth.item.api;

import com.leyou.auth.item.pojo.SpecParam;
import com.leyou.auth.item.pojo.SpecGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    @GetMapping("spec/group")
    ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid);

    @GetMapping("spec/params")
    List<SpecParam> querySpecParam(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching
    );

    @GetMapping("spec/group")
    List<SpecGroup> queryGroupByCid(@RequestParam("cid")Long cid);
}


