package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ofs.web.base.BaseEntity;
import lombok.*;

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
public class SysGroupRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String groupId;

    @TableField
    private String roleCodes;

    private String createDate;

    private String updateDate;

    private String createUser;

    private String updateUser;


    public static final String GROUP_ID = "group_id";

    public static final String ROLE_CODES = "role_codes";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
