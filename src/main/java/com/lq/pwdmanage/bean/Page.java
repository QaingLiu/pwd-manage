package com.lq.pwdmanage.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 分页对象
 * @author LQ
 * @date 2020/6/10 10:18
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int NO_ROW_OFFSET = 0;

    /* 总数 */
    private int total;

    /* 每页显示条数，默认 10 */
    private int size = 10;

    /* 总页数 */
    private int pages;

    /* 当前页 */
    private int current = 1;

    private int offset = NO_ROW_OFFSET;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    public Page() {
        /* 注意，传入翻页参数 */
    }

    /**
     * 分页构造函数
     * @param current 当前页
     * @param size    每页显示条数
     */
    public Page(int current, int size) {
        this.offset = offsetCurrent(current, size);
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
    }

    protected static int offsetCurrent(int current, int size) {
        if (current > 0) {
            return (current - 1) * size;
        }
        return 0;
    }

    public int getOffsetCurrent() {
        return offsetCurrent(this.current, this.size);
    }

    /**
     * 是否有上一页
     * @return
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否有下一页
     * @return
     */
    public boolean hasNext() {
        return this.current < this.pages;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getPages() {
        if (this.size == 0) {
            return 0;
        }
        this.pages = this.total / this.size;
        if (this.total % this.size != 0) {
            this.pages++;
        }
        return this.pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        StringBuilder pg = new StringBuilder();
        pg.append(" Page:{ [").append(super.toString()).append("], ");
        if (records != null) {
            pg.append("records-size:").append(records.size());
        } else {
            pg.append("records is null");
        }
        return pg.append(" }").toString();
    }

}
