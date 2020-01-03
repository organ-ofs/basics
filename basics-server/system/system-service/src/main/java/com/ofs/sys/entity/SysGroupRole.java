package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class SysGroupRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String groupId;

    @TableField
    private String roleCodes;


    public static final String GROUP_ID = "group_id";

    public static final String ROLE_CODES = "role_codes";
}
