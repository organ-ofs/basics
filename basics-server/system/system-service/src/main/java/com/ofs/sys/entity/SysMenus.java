package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ofs.web.base.BaseEntity;
import lombok.*;

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
public class SysMenus extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String parentId;

    @TableField
    private String grade;

    @TableField
    private String path;

    @TableField
    private String component;

    @TableField
    private String title;

    @TableField
    private String permission;

    @TableField
    private String sort;

    @TableField
    private String icon;

    @TableField
    private String hidden;

    @TableField
    private Boolean verification;

    private String description;

    private String createDate;

    private String updateDate;

    private String createUser;

    private String updateUser;

    @TableField(exist = false)
    private List<SysMenus> children;


    public static final String NAME = "name";

    public static final String PARENT_ID = "parent_id";

    public static final String GRADE = "grade";

    public static final String PATH = "path";

    public static final String COMPONENT = "component";

    public static final String TITLE = "title";

    public static final String PERMISSION = "permission";

    public static final String SORT = "sort";

    public static final String ICON = "icon";

    public static final String HIDDEN = "hidden";

    public static final String DESCRIPTION = "description";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
