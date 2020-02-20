package com.ofs.sys.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author gaoly
 * @version 2019/4/25/11:54
 */
@Data
public class ResetPasswordDto {

    @ApiModelProperty("用户登陆名")
    @NotBlank(message = "用户标识ID不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(\\w){6,18}$", message = "密码应为[A-Za-z0-9_]组成的6-18位字符！")
    private String password;

}
