package com.ofs.web.utils;

import java.io.*;

/**
 * @author: ly
 * @date: 2018/11/3 18:17
 */
public class CloneUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T object) throws Exception {

        // 此处不需要释放资源，说明：调用ByteArrayInputStream或ByteArrayOutputStream对象的close方法没有任何意义
        // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源，这一点不同于对外部资源（如文件流）的释放
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);
        oos.writeObject(object);
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bin);
        return (T) ois.readObject();
    }

}
