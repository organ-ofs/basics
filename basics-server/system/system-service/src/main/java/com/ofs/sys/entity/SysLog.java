package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ofs.web.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
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
@Accessors
@EqualsAndHashCode(callSuper = true)
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField
    @ApiModelProperty(value = "菜单ID")
    private String menuId;

    @TableField
    @ApiModelProperty(value = "动作名称,菜单增删改查")
    private String type;

    @TableField
    @ApiModelProperty(value = "描述")
    private String content;

    @TableField
    @ApiModelProperty(value = "IP")
    private String ip;

    @TableField
    @ApiModelProperty(value = "请求类型 ajax http")
    private String ajax;

    @TableField
    @ApiModelProperty(value = "URI")
    private String uri;

    @TableField
    @ApiModelProperty(value = "参数")
    private String params;

    @TableField
    @ApiModelProperty(value = "方法")
    private String httpMethod;

    @TableField
    @ApiModelProperty(value = "类")
    private String classMethod;

    public static final String MENU_ID = "menu_id";

    public static final String TYPE = "type";

    public static final String CONTENT = "content";

    public static final String IP = "ip";

    public static final String AJAX = "ajax";

    public static final String URI = "uri";

    public static final String PARAMS = "params";

    public static final String HTTP_METHOD = "http_method";

    public static final String CLASS_METHOD = "class_method";

}
