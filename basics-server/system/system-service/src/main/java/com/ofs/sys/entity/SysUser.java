package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ofs.web.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author ly
 * @since 2019-11-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField
    @ApiModelProperty(value = "登录ID")
    private String loginId;

    @TableField
    @ApiModelProperty(value = "密码")
    private String password;

    @TableField
    @ApiModelProperty(value = "用户NAME")
    private String name;

    @TableField
    @ApiModelProperty(value = "状态 1启用 0停用")
    private String status;

    @TableField
    @ApiModelProperty(value = "电话")
    private String mobile;

    @TableField
    @ApiModelProperty(value = "邮箱")
    private String email;

    @TableField
    @ApiModelProperty(value = "所属组")
    private String groupId;

    @TableField
    @ApiModelProperty(value = "最后登录时间")
    private String lastLoginDate;

    private List<SysRole> roles;
    private List<SysMenus> menus;

    public static final String LOGIN_ID = "login_id";

    public static final String PASSWORD = "password";

    public static final String NAME = "name";

    public static final String STATUS = "status";

    public static final String MOBILE = "mobile";

    public static final String EMAIL = "email";

    public static final String GROUP_ID = "group_id";

    public static final String LAST_LOGIN_DATE = "last_login_date";

}
