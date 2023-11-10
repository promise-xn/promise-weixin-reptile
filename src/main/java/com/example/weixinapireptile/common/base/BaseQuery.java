package com.example.weixinapireptile.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "基本查询对象")
@Data
public class BaseQuery {

    @ApiModelProperty(value = "页码", example = "1", required = true)
    private int pageNum = 1;

    @ApiModelProperty(value = "每页记录数", example = "10", required = true)
    private int pageSize = 10;

}
