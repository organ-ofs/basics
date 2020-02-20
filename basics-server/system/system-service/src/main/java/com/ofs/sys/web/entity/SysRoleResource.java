package com.ofs.sys.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ofs.web.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("SYS_ROLE_RESOURCE")
public class SysRoleResource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableField
    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @TableField
    @ApiModelProperty(value = "资源ID")
    private String resourceId;

    public static final String ROLE_ID = "role_id";

    public static final String RESOURCE_ID = "resource_id";
}
