package com.ofs.web.base.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @param <T>
 * @author ly
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class RequestTable<T> {
    private static final long serialVersionUID = 1L;
    private long size;
    private long current;
    private T data;

}
