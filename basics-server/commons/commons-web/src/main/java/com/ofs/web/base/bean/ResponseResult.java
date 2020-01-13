package com.ofs.web.base.bean;

import com.ofs.web.knowledge.IMessageEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gaoly
 * @version 2017/11/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "请求结果响应体")
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 8992436576262574064L;

    @ApiModelProperty(value = "响应状态回执码")
    private Integer status;

    @ApiModelProperty(value = "数据体")
    private T data;

    @ApiModelProperty(value = "响应回执消息")
    private String msg;

    @ApiModelProperty(value = "响应时间戳")
    private final long timestamps = System.currentTimeMillis();

    public synchronized static <T> ResponseResult<T> e(SystemCode statusEnum) {
        return e(statusEnum, null);
    }

    public synchronized static <T> ResponseResult<T> e(SystemCode statusEnum, T data) {
        ResponseResult<T> res = new ResponseResult<T>();
        res.setStatus(statusEnum.code);
        res.setMsg(statusEnum.msg);
        res.setData(data);
        return res;
    }

    public synchronized static <T> ResponseResult<T> success() {
        ResponseResult<T> res = new ResponseResult<T>();
        res.setStatus(ResponseCode.OK.getCode());
        res.setMsg(ResponseCode.OK.getMsg());
        res.setData(null);
        return res;
    }

    public synchronized static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setStatus(ResponseCode.OK.getCode());
        res.setMsg(ResponseCode.OK.getMsg());
        res.setData(data);
        return res;
    }

    public synchronized static <T> ResponseResult<T> failure(IMessageEnum messageEnum) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setStatus(ResponseCode.SERVER_ERROR.getCode());
        res.setMsg(messageEnum.getMessage());
        res.setData(null);
        return res;
    }

    public synchronized static <T> ResponseResult<T> auth() {
        ResponseResult<T> res = new ResponseResult<>();
        res.setStatus(ResponseCode.SERVER_ERROR.getCode());
        res.setMsg(ResponseCode.SERVER_ERROR.getMsg());
        res.setData(null);
        return res;
    }
}
