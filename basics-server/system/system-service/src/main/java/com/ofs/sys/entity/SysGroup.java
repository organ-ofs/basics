package com.ofs.sys.entity;

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
public class SysGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String description;


    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";


}
