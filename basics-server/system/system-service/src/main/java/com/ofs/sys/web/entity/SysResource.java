package com.ofs.sys.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ofs.web.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/20/16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors
@EqualsAndHashCode(callSuper = true)
public class SysResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField
    @ApiModelProperty(value = "编码")
    private String code;

    @TableField
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField
    @ApiModelProperty(value = "父ID")
    private String parentId;

    @TableField
    @ApiModelProperty(value = "类型：1 菜单 2按钮")
    private String type;

    @TableField
    @ApiModelProperty(value = "utl")
    private String url;

    @TableField
    @ApiModelProperty(value = "权限")
    private String permission;

    @TableField
    @ApiModelProperty(value = "说明")
    private String description;

    @TableField(exist = false)
    @ApiModelProperty(value = "状态：1启用，0停用")
    private String status;


    @TableField(exist = false)
    private List<String> ids;
    @TableField(exist = false)
    private String roleId;
    @TableField(exist = false)
    private List<String> roleIds;
    /**
     * 是否有权限
     */
    @TableField(exist = false)
    private String isAuth;

    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String PARENT_ID = "parent_id";

    public static final String TYPE = "type";

    public static final String MENU_ID = "menu_id";

    public static final String DESCRIPTION = "description";

}
