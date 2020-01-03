package com.ofs.web.base;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author ly
 * @version V1.0
 * @Package com.frame.common.base.model
 * @Description: 所有Entity对象继承的对象 ，实现toString，equals，hashCode方法
 * @date 2017 /3/10 14:33
 */
@Data
@EqualsAndHashCode
@ToString
public class BaseEntity implements Serializable {

    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    /**
     * 版本号,乐观锁
     */
//    @Version
//    @TableField("VERSION_NUM")
    private Integer versionNum;
    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField(value = "DELETE_FLAG")
    private String deleteFlag;

    /**
     * 创建者
     */
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    public String createUser;

    /**
     * 创建日期
     */

    @TableField(value = "CREATE_DATE", fill = FieldFill.INSERT)
    public String createDate;

    /**
     * 更新者
     */
    @TableField(value = "UPDATE_USER", fill = FieldFill.INSERT_UPDATE)
    public String updateUser;

    /**
     * 更新日期
     */
    @TableField(value = "UPDATE_DATE", fill = FieldFill.INSERT_UPDATE)
    public String updateDate;

    public static final String ID = "ID";

    public static final String VERSION_NUM = "VERSION_NUM";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_DATE = "UPDATE_DATE";

    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String DELETE_FLAG = "DELETE_FLAG";

}
