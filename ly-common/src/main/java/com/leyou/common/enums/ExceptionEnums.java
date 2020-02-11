package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnums {
    CATEGORY_NOT_FOuND(404,"商品分类没查到"),
    BRAND_NOT_FOUND(404,"品牌没查到"),
    BRAND_SAVE_ERROR(500,"品牌新增失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(400,"无效的文件类型"),
    GOODS_NOT_FOUND(500,"商品没有找到"),
    SPEC_GROUP_NOT_FOUND(400,"规格组没有找到"),
    SPEC_PARAM_NOT_FOUND(400,"规格参数没有找到"),
    GOODS_SAVE_ERROR(500,"新增商品失败"),
    GOODS_DETAIL_NOT_FOND(500,"商品详情不存在"),
    GOODS_SKU_NOT_FOND(500,"商品SKU不存在"),
    GOODS_STOCK_NOT_FOND(500,"商品库存不存在"),
    GOODS_UPDATE_ERROR(500,"商品更新失败"),
    GOODS_ID_CANNOT_BE_NULL(500,"商品id不能为null"),
    INVALID_USER_DATA_TYPE_ERROR(400,"无效的用户数据类型"),
    INVALID_CODE_ERROR(400,"验证码错误"),
    ;



    private int code;
    private String msg;


}
