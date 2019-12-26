package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
@Accessors
@EqualsAndHashCode(callSuper = true)
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField
    private String menuId;

    @TableField
    private String type;

    @TableField
    private String content;

    @TableField
    private String ip;

    @TableField
    private String ajax;

    @TableField
    private String uri;

    @TableField
    private String params;

    @TableField
    private String httpMethod;

    @TableField
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
