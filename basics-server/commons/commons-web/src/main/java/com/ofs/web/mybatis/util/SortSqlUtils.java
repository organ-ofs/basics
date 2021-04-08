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
package com.ofs.web.mybatis.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ofs.web.base.SortInfo;
import com.ofs.web.base.SortPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * SqlUtils工具类
 * </p>
 *
 * @author gaoly
 */
public class SortSqlUtils {

    private SortSqlUtils() {
    }
    private static final String PERCENT = "%";


    /**
     * 查询SQL拼接Order By
     *
     * @param originalSql 需要拼接的SQL
     * @param page        page对象
     * @param orderBy     是否需要拼接Order By
     * @return SQL
     */
    public static String concatOrderBy(String originalSql, SortPage page, boolean orderBy) {
        if (orderBy && CollectionUtils.isNotEmpty(page.getOrders())) {
            StringBuilder buildSql = new StringBuilder(originalSql);
            String orderStr = concatOrderBuilder(page.getOrders());
            if (StringUtils.isNotEmpty(orderStr)) {
                buildSql.append(" ORDER BY ").append(orderStr);

            }
            return buildSql.toString();
        }
        return originalSql;
    }

    /**
     * 拼接多个排序方法
     *
     * @param orders
     */
    private static String concatOrderBuilder(List<SortInfo> orders) {

        if (CollectionUtils.isNotEmpty(orders)) {
            return orders.stream().filter(ObjectUtils::isNotEmpty)
                    .map(sortInfo -> sortInfo.toString())
                    .collect(Collectors.joining(StringPool.COMMA));
        }
        return StringUtils.EMPTY;

    }
    /**
     * 模糊查询，拼接%，全模糊
     * @param param 拼接参数
     * @return 拼接结果
     */
    public static String getLikeParam(String param) {
        return org.apache.commons.lang3.StringUtils.isBlank(param) ? "" : PERCENT + param + PERCENT;
    }
    /**
     * 模糊查询，拼接%，前模糊
     * @param param 拼接参数
     * @return 拼接结果
     */
    public static String getLikeBeforeParam(String param) {
        return org.apache.commons.lang3.StringUtils.isBlank(param) ? "" : PERCENT + param;
    }
    /**
     * 模糊查询，拼接%，后模糊
     * @param param 拼接参数
     * @return 拼接结果
     */
    public static String getLikeAfterParam(String param) {
        return org.apache.commons.lang3.StringUtils.isBlank(param) ? "" : param + PERCENT;
    }


}
