package com.ofs.utils;

import java.util.UUID;

/**
 * @author gaoly
 */
public class IdentifierUtils {

    /**
     * 生成UUID 32 位
     *
     * @return
     */
    public static synchronized String nextUuid() {
        return UUID.randomUUID().toString().replaceAll("_", "");
    }

    /**
     * 生成ID 递增 TODO
     *
     * @return
     */
    public static synchronized long nextId() {
        return 0;
    }
}
