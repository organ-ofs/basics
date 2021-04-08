package com.ofs.web.mybatis.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoly
 */
@Slf4j
public class FunDataUtils {
    private FunDataUtils() {

    }

    public static String getLevelCode(String code) {
        Integer index = Integer.parseInt(code.substring(code.length() - 4, code.length())) + 1;
        return code.substring(0, code.length() - 4) + FunDataUtils.getSeqCode(index);
    }

    public static String getSeqCode(Integer code) {
        String tempCode = "";
        if (code > 1000) {
            tempCode = code.toString();
        } else if (code > 100) {
            tempCode = "0" + code;
        } else if (code > 10) {
            tempCode = "00" + code;
        } else {
            tempCode = "000" + code;
        }
        return tempCode;
    }
}
