package com.ofs.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.ofs.web.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author gaoly
 * @version 2019/4/16/11:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String userId;

    private String roleId;

    public static final String USER_ID = "USER_ID";

    public static final String ROLE_ID = "ROLE_ID";
}
