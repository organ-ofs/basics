/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ofs.web.base;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.ibatis.session.RowBounds.NO_ROW_LIMIT;

/**
 * 简单分页模型
 *
 * @author ly
 */
public class SortPage<T> extends Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public SortPage() {
        super();
    }

    public SortPage(int size) {
        super();
        this.setSize(size);
    }

    /**
     * 分页排序信息
     */
    private List<SortInfo> orders = new ArrayList<>();

    /**
     * 兼容旧的逻辑
     *
     * @param columns
     * @param direction
     */
    private void orders(List<String> columns, SortInfo.Direction direction) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(columns)) {
            columns.forEach(column -> orders.add(new SortInfo(column, direction))
            );
        }
    }

    /**
     * 取得排序信息
     *
     * @return
     */
    public List<SortInfo> getOrders() {
        if (ArrayUtils.isNotEmpty(this.ascs())) {
            this.orders(Arrays.asList(this.ascs()), SortInfo.Direction.ASC);
        }

        if (ArrayUtils.isNotEmpty(this.descs())) {
            this.orders(Arrays.asList(this.descs()), SortInfo.Direction.DESC);
        }
        return orders;
    }

    /**
     * 构造函数
     *
     * @param current
     * @param size
     */
    public SortPage(int current, int size) {
        super(current, size);
    }


    /**
     * 构造函数
     *
     * @param current
     * @param size
     * @param defaultSortString
     * @param sortString
     */
    public SortPage(int current, int size, String defaultSortString, String sortString, boolean camelToUnderlineFlg) {
        this(current, size);

        if (StringUtils.isNotEmpty(defaultSortString)) {
            this.orders.addAll(SortInfo.formStringToList(defaultSortString, camelToUnderlineFlg));
        }

        if (StringUtils.isNotEmpty(sortString)) {
            this.orders.addAll(SortInfo.formStringToList(sortString, camelToUnderlineFlg));
        }
    }


    @Override
    public String toString() {
        StringBuilder pg = new StringBuilder();
        pg.append(" SortPage:{ [").append(super.toString()).append("], ");
        if (this.getRecords() != null) {
            pg.append("records-size:").append(this.getRecords().size());
        } else {
            pg.append("records is null");
        }
        return pg.append(" }").toString();
    }

    /**
     * 取得实例对象
     *
     * @param isPage
     * @return
     */
    public static <T> Page<T> getInstance(boolean isPage) {
        if (isPage) {
            return new SortPage<>();
        } else {
            return new SortPage<>(NO_ROW_LIMIT);
        }
    }
}
