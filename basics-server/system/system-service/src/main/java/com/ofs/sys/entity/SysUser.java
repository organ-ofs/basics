package com.ofs.sys.entity;

import com.ofs.web.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String loginId;

    private String password;

    private String name;

    private String mobile;

    private String email;

    private String groupId;

    private String lastLoginDate;

    private String createDate;

    private String updateDate;

    private String createUser;

    private String updateUser;

    public static final String LOGIN_ID = "login_id";

    public static final String PASSWORD = "password";

    public static final String NAME = "name";

    public static final String MOBILE = "mobile";

    public static final String EMAIL = "email";

    public static final String GROUP_ID = "group_id";

    public static final String LAST_LOGIN_DATE = "last_login_date";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
