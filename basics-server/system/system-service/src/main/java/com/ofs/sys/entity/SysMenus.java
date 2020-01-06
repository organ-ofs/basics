package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ofs.web.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ly
 * @since 2019-11-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors
@EqualsAndHashCode(callSuper = true)
public class SysMenus extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @TableField
    @ApiModelProperty(value = "父ID")
    private String parentId;

    @TableField
    @ApiModelProperty(value = "资源ID")
    private String resourceId;

    @TableField
    @ApiModelProperty(value = "1 级菜单 2 级菜单……")
    private String grade;

    @TableField
    @ApiModelProperty(value = "菜单path")
    private String path;

    @TableField
    @ApiModelProperty(value = "菜单对应资源路径")
    private String component;

    @TableField
    @ApiModelProperty(value = "抬头")
    private String title;

    @TableField
    @ApiModelProperty(value = "权限标识，未用")
    private String permission;

    @TableField
    @ApiModelProperty(value = "排序")
    private String sort;

    @TableField
    @ApiModelProperty(value = "图标")
    private String icon;

    @TableField
    @ApiModelProperty(value = "0 禁用 1 启用")
    private String hidden;

    @TableField
    @ApiModelProperty(value = "是否需要权限验证")
    private Boolean verification;

    @TableField
    @ApiModelProperty(value = "说明")
    private String description;

    @TableField(exist = false)
    private List<SysMenus> children;


    public static final String NAME = "name";

    public static final String PARENT_ID = "parent_id";

    public static final String RESOURCE_ID = "resource_id";

    public static final String GRADE = "grade";

    public static final String PATH = "path";

    public static final String COMPONENT = "component";

    public static final String TITLE = "title";

    public static final String PERMISSION = "permission";

    public static final String SORT = "sort";

    public static final String ICON = "icon";

    public static final String HIDDEN = "hidden";

    public static final String DESCRIPTION = "description";

}
