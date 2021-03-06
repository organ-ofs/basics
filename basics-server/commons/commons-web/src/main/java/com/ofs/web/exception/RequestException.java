package com.ofs.web.exception;

import com.ofs.web.base.bean.SystemCode;
import lombok.*;

import java.io.Serializable;

/**
 * @author gaoly
 * @version 2019/11/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestException extends RuntimeException implements Serializable {
    private String status;
    private String msg;
    private Exception e;

    public RequestException(SystemCode statusEnum, Exception e) {
        this.status = statusEnum.code;
        this.msg = statusEnum.message;
        this.e = e;
    }


    public RequestException(SystemCode statusEnum) {
        this.status = statusEnum.code;
        this.msg = statusEnum.message;
    }

    public synchronized static RequestException fail(String msg) {
        return RequestException.builder()
                .status(SystemCode.FAIL.code)
                .msg(msg)
                .build();
    }

    public synchronized static RequestException fail(String msg, Exception e) {
        return RequestException.builder()
                .status(SystemCode.FAIL.code)
                .msg(msg)
                .e(e)
                .build();
    }

    public synchronized static RequestException fail(String code, String msg, Exception e) {
        return RequestException.builder()
                .status(code)
                .msg(msg)
                .e(e)
                .build();
    }


}
