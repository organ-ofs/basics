package com.ofs.sys.entity;

import com.ofs.web.base.BaseEntity;
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

    private String loginId;

    private String password;

    private String name;

    private String status;

    private String mobile;

    private String email;

    private String groupId;

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
