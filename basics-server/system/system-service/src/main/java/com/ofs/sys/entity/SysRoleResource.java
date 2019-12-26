package com.ofs.sys.entity;

import com.ofs.web.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author gaoly
 * @version 2019/4/16/8:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors
public class SysRoleResource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String roleId;

    private String resourceId;

    public static final String ROLE_ID = "role_id";

    public static final String RESOURCE_ID = "resource_id";
}
