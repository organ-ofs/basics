package com.ofs.web.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;


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
public class BaseDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 版本号
     */
    @ApiModelProperty("VERSION_NUM")
    private Integer versionNum;
    /**
     * 逻辑删除标识
     */
    @ApiModelProperty(value = "DELETE_FLAG")
    private String deleteFlag;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "CREATE_USER")
    protected String createUser;

    /**
     * 创建日期
     */

    @ApiModelProperty(value = "CREATE_DATE")
    protected LocalDateTime createDate;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "UPDATE_USER")
    protected String updateUser;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "UPDATE_DATE")
    protected LocalDateTime updateDate;

    public static final String ID = "ID";

    public static final String VERSION_NUM = "VERSION_NUM";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_DATE = "UPDATE_DATE";

    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String DELETE_FLAG = "DELETE_FLAG";

    public static final String VERSION_NUM_FILED = "versionNum";


    public static final String ID_FILED = "id";
}
