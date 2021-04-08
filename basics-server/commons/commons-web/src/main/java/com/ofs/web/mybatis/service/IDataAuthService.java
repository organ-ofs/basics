package com.ofs.web.mybatis.service;

/**
 * <p>
 * 只能数据权限设置  服务类
 * </p>
 *
 * @author hexueyang
 * @since 2018-08-27
 */
public interface IDataAuthService {
    /**
     * 根根据账号和模块代码获取sql查询条件
     *
     * @param authId    id
     * @param moduleCode 模块编码
     * @return 查询结果
     * @author hexueyang
     */
    String getDataAuth(String authId, String moduleCode);

}
