package com.example.weixinapireptile.common.result;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页响应对象
 *
 * @author zhanglincheng
 * @date 2022/10/1
 */
@Data
public class PageResult<T> implements Serializable {

    /**
     * 响应状态码
     */
    private String status;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 数据记录
     */
    private List<T> record;

    /**
     * 数据总数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 响应分页数据
     *
     * @param list select查询到的数据
     * @return PageResult<T>
     */
    public static <T> PageResult<T> success(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return getInstance(pageInfo);
    }

    /**
     * 响应分页数据（适配PageHelper的PageInfo对象）
     */
    public static <T> PageResult<T> success(PageInfo<T> pageInfo) {
        return getInstance(pageInfo);
    }

    /**
     * 响应分页数据（适配PageHelper的Page对象）
     */
    @Deprecated
    public static <T> PageResult<T> success(Page<T> page) {
        return getInstance(page);
    }

    /**
     * 响应分页数据（适配MybatisPlus的Page对象）
     */
    @Deprecated
    public static <T> PageResult<T> success(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return getInstance(page);
    }

    private static <T> PageResult<T> getInstance(Object obj) {
        PageResult<T> pageResult = new PageResult<T>() {{
            setStatus(ResultStatus.SUCCESS.getStatus());
            setMessage(ResultStatus.SUCCESS.getMessage());
        }};
        if (obj instanceof PageInfo) {
            // 适配PageHelper的PageInfo对象
            PageInfo<T> pageInfo = (PageInfo<T>) obj;
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPages(pageInfo.getPages());
            pageResult.setCurrentPage(pageInfo.getPageNum());
            pageResult.setRecord(pageInfo.getList());
        } else if (obj instanceof Page) {
            // 适配PageHelper的Page对象
            Page<T> page = (Page<T>) obj;
            pageResult.setTotal(page.getTotal());
            pageResult.setPages(page.getPages());
            pageResult.setCurrentPage(page.getPageNum());
            pageResult.setRecord(page.getResult());
        } else if (obj instanceof com.baomidou.mybatisplus.extension.plugins.pagination.Page) {
            // 适配MybatisPlus的Page对象
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page =
                    (com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>) obj;
            pageResult.setTotal(page.getTotal());
            pageResult.setPages((int) page.getPages());
            pageResult.setCurrentPage((int) page.getCurrent());
            pageResult.setRecord(page.getRecords());
        } else {
            throw new RuntimeException("不支持的分页对象");
        }
        return pageResult;
    }

}
