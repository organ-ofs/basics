package com.ofs.web.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XML工具类
 *
 * @author gaoly
 */
public class XmlUtil {

    private XmlUtil() {
    }

    /**
     * XML转java对象
     *
     * @param clazz 类型
     * @param xml   XML
     * @return java对象
     * @throws JAXBException
     */
    public static <T> T xmlToObject(Class<T> clazz, String xml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        @SuppressWarnings("unchecked")
        T obj = (T) unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
        return obj;
    }

    /**
     * java对象转xml
     *
     * @param obj java对象
     * @return xml
     * @throws JAXBException
     */
    public static String objectToXml(Object obj) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jc.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}
