package com.ofs.sys.web.entity;

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
public class SysGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField
    @ApiModelProperty(value = "CODE")
    private String code;

    @TableField
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField
    @ApiModelProperty(value = "说明")
    private String description;


    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";


}
