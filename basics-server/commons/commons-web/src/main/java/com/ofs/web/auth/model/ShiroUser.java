package com.ofs.web.auth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import java.io.Serializable;

/**
 * @author ly
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
@NoArgsConstructor
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String id;
    /**
     * jwtID
     */
    private String jwtId;

    /**
     * 用户账户
     */
    private String account;

    private String loginId;
    private String password;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 终端
     */
    private String terminal;

    /**
     * 模拟account的账户
     */
    private String runAsAccount;


    /**
     * 构造函数
     *
     * @param account 账户
     * @param name    姓名
     * @param authId  ID
     */
    public ShiroUser(String account, String name, String authId) {
        this.account = account;
        this.name = name;
        this.id = id;
    }


    /**
     * 取得用户信息
     *
     * @return
     */
    public static ShiroUser getCurrentUser() {

        return (ShiroUser) SecurityUtils.getSubject().getPrincipal();

    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return account + (StringUtils.isNotEmpty(runAsAccount) ? "_" + runAsAccount : "");
    }
}
