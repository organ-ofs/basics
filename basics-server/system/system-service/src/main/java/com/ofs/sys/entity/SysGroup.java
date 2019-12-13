package com.ofs.sys.entity;

import com.ofs.web.base.BaseEntity;
import lombok.*;

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
public class SysGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String description;

    private String createDate;

    private String updateDate;

    private String createUser;

    private String updateUser;


    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
