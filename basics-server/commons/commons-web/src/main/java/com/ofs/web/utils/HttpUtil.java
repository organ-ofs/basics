package com.ofs.web.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * HTTP相关的工具类
 *
 * @author gaoly
 */
public class HttpUtil {

    private HttpUtil() {

    }

    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String HTTP_HEAD_ACCEPT_K = "dataType";
    private static final String HTTP_HEAD_ACCEPT_V = "application/json";

    /**
     * 对URL做get请求
     *
     * @param url     URL
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static <T> T getJson(String url, Class<T> type, BasicHeader... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                for (int i = 0; i < headers.length; i++) {
                    BasicHeader itemHeader = headers[i];
                    httpGet.addHeader(itemHeader.getName(), itemHeader.getValue());
                }
            }
            httpGet.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpGet.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);


            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }

    /**
     * 对URL做get请求
     *
     * @param url     URL
     * @param param   JSON对象参数
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static <T> T getJson(String url, Object param, Class<T> type, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (param != null) {
                url += "?" + URLtoUTF8Util.convertJsonToUrl(param);
            }
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                httpGet.setHeaders(headers);
            }
            httpGet.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpGet.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }

    /**
     * 对URL做get请求
     *
     * @param url       URL
     * @param paramJson JSON对象参数
     * @param headers   自定义请求头
     * @return 返回值 String
     * @throws Exception
     */
    public static String getJson(String url, JSON paramJson, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (paramJson != null) {
                url += "?" + URLtoUTF8Util.convertJsonToUrl((JSONObject) paramJson);
            }
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                httpGet.setHeaders(headers);
            }
            httpGet.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpGet.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return jsonStr;
        }
    }

    /**
     * 对URL做post请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url     URL
     * @param param   POST的内容
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static <T> T postJson(String url, Object param, Class<T> type, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                httpPost.setHeaders(headers);
            }
            httpPost.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpPost.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            StringEntity sendEntity = new StringEntity(FastJsonUtil.toJson(param));
            httpPost.setEntity(sendEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }

    /**
     * 对URL做post请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url     URL
     * @param param   POST的内容
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws IOException
     */
    public static <T> T postJson(String url, Object param, TypeReference<T> type, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                httpPost.setHeaders(headers);
            }
            httpPost.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpPost.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            StringEntity sendEntity = new StringEntity(FastJsonUtil.toJson(param));
            httpPost.setEntity(sendEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }


    /**
     * 对URL做post请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url       URL
     * @param paramJson POST的内容
     * @param headers   自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static String postJson(String url, JSON paramJson, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                httpPost.setHeaders(headers);
            }
            httpPost.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpPost.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            StringEntity sendEntity = new StringEntity(paramJson.toJSONString());
            httpPost.setEntity(sendEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return jsonStr;
        }
    }

    /**
     * 对URL做delete请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url     URL
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static <T> T deleteJson(String url, Class<T> type, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(url);
            if (headers != null) {
                httpDelete.setHeaders(headers);
            }
            httpDelete.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpDelete.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }


    /**
     * 对URL做delete请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url     URL
     * @param param   JSON对象参数
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static <T> T deleteJson(String url, Object param, Class<T> type, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (param != null) {
                url += "?" + URLtoUTF8Util.convertJsonToUrl(param);
            }
            HttpDelete httpDelete = new HttpDelete(url);
            if (headers != null) {
                httpDelete.setHeaders(headers);
            }
            httpDelete.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpDelete.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            HttpEntity recvEntity = response.getEntity();
            //删除动作相应体为空
            String jsonStr = "";
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }


    /**
     * 对URL做delete请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url       URL
     * @param paramJson JSON对象参数
     * @param headers   自定义请求头
     * @return 返回值 String
     * @throws Exception
     */
    public static String deleteJson(String url, JSON paramJson, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (paramJson != null) {
                url += "?" + URLtoUTF8Util.convertJsonToUrl((JSONObject) paramJson);
            }
            HttpDelete httpDelete = new HttpDelete(url);
            if (headers != null) {
                httpDelete.setHeaders(headers);
            }
            httpDelete.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpDelete.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            HttpEntity recvEntity = response.getEntity();
            //删除动作相应体为空
            String jsonStr = "";
            EntityUtils.consume(recvEntity);
            return jsonStr;
        }
    }


    /**
     * 对URL做put请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url     URL
     * @param param   PUT的内容
     * @param type    返回值类型
     * @param headers 自定义请求头
     * @return 返回值对象
     * @throws Exception
     */
    public static <T> T putJson(String url, Object param, Class<T> type, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            if (headers != null) {
                httpPut.setHeaders(headers);
            }
            httpPut.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpPut.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            StringEntity sendEntity = new StringEntity(FastJsonUtil.toJson(param));
            httpPut.setEntity(sendEntity);
            CloseableHttpResponse response = httpclient.execute(httpPut);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return FastJsonUtil.getObject(jsonStr, type);
        }
    }

    /**
     * 对URL做put请求，URL返回值为JSON格式，将返回的JSON转换为Object
     *
     * @param url       URL
     * @param paramJson PUT的内容
     * @param headers   自定义请求头
     * @return 返回值 String
     * @throws Exception
     */
    public static String putJson(String url, JSON paramJson, Header... headers) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            if (headers != null) {
                httpPut.setHeaders(headers);
            }
            httpPut.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            httpPut.setHeader(HTTP_HEAD_ACCEPT_K, HTTP_HEAD_ACCEPT_V);
            StringEntity sendEntity = new StringEntity(paramJson.toJSONString());
            httpPut.setEntity(sendEntity);
            CloseableHttpResponse response = httpclient.execute(httpPut);
            HttpEntity recvEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(recvEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(recvEntity);
            return jsonStr;
        }
    }

}
