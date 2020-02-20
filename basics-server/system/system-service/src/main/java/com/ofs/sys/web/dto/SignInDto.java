package com.ofs.sys.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ly
 * @version 2019/10/24
 */
@Data
public class SignInDto {

    @ApiModelProperty("登陆名")
    @NotBlank(message = "用户名不可以为空！")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不可以为空！")
    private String password;

    /**
     * 终端
     */
    private String terminal;

}
