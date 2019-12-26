package com.ofs.sys.entity;

import com.ofs.web.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author gaoly
 * @version 2019/4/16/11:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Accessors
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String roleId;

    public static final String USER_ID = "USER_ID";

    public static final String ROLE_ID = "ROLE_ID";
}
