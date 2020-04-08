package com.ofs.web.base.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @param <T>
 * @author ly
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ResultTable<T> extends Result<List<T>> {
    private static final long serialVersionUID = 1L;


    //"总页数")
    private long pages;

    //"总条数")
    private long total;

    //"当前页码")
    private long current;

    //"页码大小")
    private long size;

    public ResultTable() {
        super();
    }

    public ResultTable(Page<T> page) {
        super();
        this.total = page.getTotal();
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.pages = page.getPages();
        super.setData(page.getRecords());
    }

    public ResultTable(IPage<T> page) {
        super();
        this.total = page.getTotal();
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.pages = page.getPages();
        super.setData(page.getRecords());
    }

    /**
     * 返回所有数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultTable result(List<T> data) {
        ResultTable result = new ResultTable();
        result.setCurrent(1);
        result.setTotal(data.size());
        result.setSize(data.size());
        result.setData(data);
        return result;
    }

    /**
     * 分页
     *
     * @param data
     * @return
     */
    public static <T> ResultTable result(IPage<T> data) {
        return new ResultTable(data);
    }

}
