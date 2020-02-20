package com.ofs.sys.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ofs.web.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

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
@Accessors
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField
    @ApiModelProperty(value = "角色编号")
    private String code;

    @TableField
    @ApiModelProperty(value = "角色名称")
    private String name;

    @TableField
    @ApiModelProperty(value = "说明")
    private String description;

    @TableField(exist = false)
    private List<SysResource> resources;


    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

}
