package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ofs.web.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class SysResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String parentId;

    //1 菜单 2按钮
    private String type;

    private String url;

    private String permission;

    private String description;

    @TableField(exist = false)
    private List<SysResource> children;

    /**
     * 是否有子节点
     */
    private Boolean leaf;
    private List<String> ids;
    private String roleId;

    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String PARENT_ID = "parent_id";

    public static final String TYPE = "type";

    public static final String MENU_ID = "menu_id";

    public static final String DESCRIPTION = "description";

}
