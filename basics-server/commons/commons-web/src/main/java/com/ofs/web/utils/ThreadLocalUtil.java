package com.ofs.web.utils;

import java.util.Locale;

/**
 * 线程间通信
 *
 * @author ly
 */
public class ThreadLocalUtil {
    private ThreadLocalUtil() {

    }

    /**
     * 多语言
     */
    private static ThreadLocal<Locale> localeLang = new ThreadLocal<>();

    /**
     * 秘钥
     */
    private static ThreadLocal<String> localeSecret = new ThreadLocal<>();

    public static String getLocaleSecret() {
        return localeSecret.get();
    }

    public static void setLocaleSecret(String secret) {
        localeSecret.set(secret);
    }

    public static Locale getLocaleLang() {
        Locale locale = localeLang.get();
        if (locale == null) {
            locale = Locale.CHINA;
        }
        return locale;
    }

    public static void setLocaleLang(Locale lang) {
        localeLang.set(lang);
    }

    public static void removeLocaleLang() {
        localeLang.remove();
    }


    public static void removeLocaleSecret() {
        localeSecret.remove();
    }

}
