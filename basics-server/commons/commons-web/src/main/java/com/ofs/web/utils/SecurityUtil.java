package com.ofs.web.utils;


import com.ofs.web.constant.BaseDataConstant;
import com.ofs.web.exception.GlobalErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import static com.ofs.web.knowledge.FrameMessageEnum.*;

/**
 * @author: ly
 * @date: 2018/7/23 17:46
 */
@Slf4j
public class SecurityUtil {

    /**
     * 对称加密算法
     *
     * @author zhouzhian
     */
    public static class AesUtil {
        private static final String ALGORITHM = "AES";
        private static final String CIPHER_ALGORITHM = "AES/CBC/NoPadding";


        /*
         * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-EBC加密模式，key需要为16位。
         */

        /**
         * 加密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */

        public static String encrypt(String data, String key) {
            try {
                if (key == null) {
                    log.error("AesUtil encrypt Key为空null");
                    throw new GlobalErrorException(ENCRYPT_ERROR);
                }
                // 判断Key是否为16位
                if (key.length() != 16) {
                    log.error("AesUtil encrypt Key长度不是16位");

                    throw new GlobalErrorException(ENCRYPT_ERROR);
                }
                //"算法/模式/补码方式"NoPadding PkcsPadding
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                // 使用CBC模式，需要一个向量iv，可增加加密算法的强度 ,此处设置和KEY 一样
                IvParameterSpec iv = new IvParameterSpec(key.getBytes());
                byte[] dataBytes = data.getBytes();

                int plaintextLength = dataBytes.length;

                int blockSize = cipher.getBlockSize();
                if (plaintextLength % blockSize != 0) {
                    plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
                }

                byte[] plaintext = new byte[plaintextLength];
                System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
                SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);

                cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
                byte[] encrypted = cipher.doFinal(plaintext);

                return new Base64().encodeToString(encrypted);

            } catch (Exception e) {
                log.error("encrypt:", e);
                throw new GlobalErrorException(ENCRYPT_ERROR);
            }


        }

        /**
         * 解密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        public static String decrypt(String data, String key) {
            try {
                // 判断Key是否正确
                if (key == null) {
                    log.error("AesUtil decrypt Key为空null");
                    throw new GlobalErrorException(DECRYPT_ERROR);
                }
                // 判断Key是否为16位
                if (key.length() != 16) {
                    log.error("AesUtil decrypt Key 不为16位");
                    throw new GlobalErrorException(DECRYPT_ERROR);
                }
                byte[] encrypted = new Base64().decode(data);

                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

                // 使用CBC模式，需要一个向量iv，可增加加密算法的强度 ,此处设置和KEY 一样
                IvParameterSpec iv = new IvParameterSpec(key.getBytes());
                SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);

                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original, BaseDataConstant.DEFAULT_CHARSET);
                return originalString;
            } catch (Exception e) {

                log.error("decrypt:", e);
                throw new GlobalErrorException(DECRYPT_ERROR);
            }
        }

    }

    public static class RsaUtils {
        /** */
        /**
         * 加密算法RSA
         */
        public static final String KEY_ALGORITHM = "RSA";

        /** */
        /**
         * 签名算法
         */
        public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

        /** */
        /**
         * 获取公钥的key
         */
        private static final String PUBLIC_KEY = "RSAPublicKey";

        /** */
        /**
         * 获取私钥的key
         */
        private static final String PRIVATE_KEY = "RSAPrivateKey";

        /** */
        /**
         * RSA最大加密明文大小
         */
        private static final int MAX_ENCRYPT_BLOCK = 117;

        /** */
        /**
         * RSA最大解密密文大小
         */
        private static final int MAX_DECRYPT_BLOCK = 128;

        /** */
        /**
         * <p>
         * 生成密钥对(公钥和私钥)
         * </p>
         *
         * @return
         * @throws Exception
         */
        public static Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        }

        /** */
        /**
         * <p>
         * 用私钥对信息生成数字签名
         * </p>
         *
         * @param data       已加密数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static String sign(byte[] data, String privateKey) {
            try {

                byte[] keyBytes = Base64.decodeBase64(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
                signature.initSign(privateK);
                signature.update(data);
                return Base64.encodeBase64String(signature.sign());
            } catch (Exception e) {
                log.error("sign:", e);
                throw new GlobalErrorException(SIGN_ERROR);
            }
        }

        /** */
        /**
         * <p>
         * 校验数字签名
         * </p>
         *
         * @param data      已加密数据
         * @param publicKey 公钥(BASE64编码)
         * @param sign      数字签名
         * @return
         * @throws Exception
         */
        public static boolean verify(byte[] data, String publicKey, String sign) {
            try {

                byte[] keyBytes = Base64.decodeBase64(publicKey);

                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey publicK = keyFactory.generatePublic(keySpec);
                Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
                signature.initVerify(publicK);
                signature.update(data);
                return signature.verify(Base64.decodeBase64(sign));
            } catch (Exception e) {
                log.error("verify:", e);
                throw new GlobalErrorException(VERIFY_ERROR);
            }
        }

        /** */
        /**
         * <P>
         * 私钥解密
         * </p>
         *
         * @param encryptedDataStr 已加密数据
         * @param privateKey       私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static String decryptByPrivateKey(String encryptedDataStr, String privateKey) {
            try (
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
            ) {
                byte[] keyBytes = Base64.decodeBase64(privateKey);
                byte[] encryptedData = Base64.decodeBase64(encryptedDataStr);

                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateK);
                int inputLen = encryptedData.length;
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                byte[] decryptedData = out.toByteArray();
                return new String(decryptedData, BaseDataConstant.DEFAULT_CHARSET);
            } catch (Exception e) {
                log.error("decryptByPrivateKey:", e);
                throw new GlobalErrorException(DECRYPT_ERROR);
            }
        }

        /** */
        /**
         * <p>
         * 公钥解密
         * </p>
         *
         * @param encryptedDataStr 已加密数据
         * @param publicKey        公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static String decryptByPublicKey(byte[] encryptedDataStr, String publicKey) {
            try (
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

            ) {
                byte[] keyBytes = Base64.decodeBase64(publicKey);
                byte[] encryptedData = Base64.decodeBase64(encryptedDataStr);

                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key publicK = keyFactory.generatePublic(x509KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, publicK);
                int inputLen = encryptedData.length;
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                byte[] decryptedData = out.toByteArray();
                return new String(decryptedData, BaseDataConstant.DEFAULT_CHARSET);
            } catch (Exception e) {
                log.error("decryptByPublicKey:", e);
                throw new GlobalErrorException(DECRYPT_ERROR);
            }
        }

        /** */
        /**
         * <p>
         * 公钥加密
         * </p>
         *
         * @param dataStr   源数据
         * @param publicKey 公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static String encryptByPublicKey(String dataStr, String publicKey) {
            try (
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
            ) {
                byte[] keyBytes = Base64.decodeBase64(publicKey);
                byte[] data = dataStr.getBytes(BaseDataConstant.DEFAULT_CHARSET);

                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key publicK = keyFactory.generatePublic(x509KeySpec);
                // 对数据加密
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, publicK);
                int inputLen = data.length;
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                byte[] encryptedData = out.toByteArray();
                return Base64.encodeBase64String(encryptedData);
            } catch (Exception e) {
                log.error("encryptByPublicKey:", e);
                throw new GlobalErrorException(ENCRYPT_ERROR);
            }
        }

        /** */
        /**
         * <p>
         * 私钥加密
         * </p>
         *
         * @param dataStr    源数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static String encryptByPrivateKey(String dataStr, String privateKey) {
            try (
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
            ) {
                byte[] keyBytes = Base64.decodeBase64(privateKey);
                byte[] data = dataStr.getBytes(BaseDataConstant.DEFAULT_CHARSET);

                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, privateK);
                int inputLen = data.length;
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                byte[] encryptedData = out.toByteArray();
                return Base64.encodeBase64String(encryptedData);
            } catch (Exception e) {
                log.error("encryptByPrivateKey:", e);
                throw new GlobalErrorException(ENCRYPT_ERROR);
            }
        }

        /** */
        /**
         * <p>
         * 获取私钥
         * </p>
         *
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        public static String getPrivateKey(Map<String, Object> keyMap) {
            Key key = (Key) keyMap.get(PRIVATE_KEY);
            return Base64.encodeBase64String(key.getEncoded());
        }

        /** */
        /**
         * <p>
         * 获取公钥
         * </p>
         *
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        public static String getPublicKey(Map<String, Object> keyMap) {
            Key key = (Key) keyMap.get(PUBLIC_KEY);
            return Base64.encodeBase64String(key.getEncoded());
        }

    }

    public static void main(String[] args) {
        // 需要加密的字串
//        String cSrc = "我爱你";
//        String key = "5e463499c1a57ad8";
//        System.out.println("加密密钥是：" + key);
//
//        // 加密
//        long lStart = System.currentTimeMillis();
//        String enString = AesUtil.encrypt(cSrc, key);
//        System.out.println("加密后的字串是：" + enString);
//
//        long lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("加密耗时：" + lUseTime + "毫秒");
//        // 解密
//        lStart = System.currentTimeMillis();
////        String enString = "CviZ6NUDF9PBv1Y7lIdFPnvsIFr/bdEfwrxxvInlVew=";
//        String DeString = AesUtil.decrypt("jZ3HXwcvGofAc4Bhau+grnM1B5b8W/paHY9FQSgUtHgH5JqeuyEqy/Ez0+nKX6V+pXZ94AI6SMdxQ5nseF2HojYtEjWu7rlNkS7uXhdB3Qq9fxLG2XlSBmvDQYmrnoIbehuQ2dRBph+POZynOIQ79FU713ajP438xwnT/h/io/59wBbEjujbm0SE8QVQM0ShGn9fJRDiZyHYxKCAqCzn23waYD9zaBn4kPIeu5S7F1SkfDp8Z8Qv8qux59x9/nFgEf8NeSDaeUdtDIGQ7IQfLI8N8WxmBll7uBHeA6JQ2ZobLW3iYMguUq1H+Lp0c1uFiYg6+nf7C6NwH2cXlq/bKQzTYUY1YGtIp+a9uexvyQH4HX9QiZQC+0ZwA44i2YW6gPE6R+PK3szlH0FBT/nxjSCDD9+Z6iot2tlz6T3DKf5iPdBK+hVU+eLLFYklLjFmfhNgi1LrJGJauNFxzahryMXZb1/oGlGxoBM9ofGjzW2RfICI1bXn4bfsiGa6Q5BRiILqRSfRv5A0ETLwxaGae8GJUMHu/apk98YnjluOib7wj0ASD291f1BwBgZTw7iu8TL91+zr/4CtA6EE05m41rmlUK9cPVt1qadAnCoUl1rvvNL6HFY3h0lQHB69ibzYl0jFA4zuq6HCWWRVQ26V9/jXovrNGL509u8EzEt8EgqaNChJ076Nc9wJNaPrtFFcM5tAtCcgxuQNdmeCl2fjrKZI0uwSgano85hWJyf6tc6xYcJbHiCYqD7nOhJ+WQxwJgxXnRt4B1MW2eu/A3ma1qt4lh5dDtFHRPnpwPu3k9mIgupFJ9G/kDQRMvDFoZp7kzllCOWaC6ZEud5XYu6HjSYz2vl5kdTxzPXFcSdJVWanqoiZnLy7fuIL1Pu7+JHGD2htrP4Uzb27dzAZyLkimExqhZepX+Ho988NDinQyJzbudr6jKYS07Tn3/EJXY/1W9FqHidY/Vkt2UMWl1lL4vlo39Esvrojw5SF65iT9T6f92yMf5YrKon7ns5YEhwg5POUu/bIJjdmZSkOxul6dTYHYJgSZoDb3qTkyobXeGTUVFIvSQ6rtB33MzfK/ykh3Kv4w5v53HamyzopdrC7iiwARkj03q/WYlCbsvRQaw87ZNs3hSyUq9mW0MipBaos4xN/NySqeGdveXz7KZ40BcZxQ3e/L618e/hMn2ag+HpTJGD+987ii+ka8uY3cJqduWrqW6nxl1G/mHqiKz9uaxvUe5Iwg0bORgE3E36icO7cIXjSrsjVxOlyGfYzu+95N414WGKrbpqWM2516PkUGb9VrK7SBVnox5H3k1mcuhTpZpK/HazTcq9Trs/5iqnzL5WbnxVEmRNQno8kQv+TaBpDGQiRpJoEGiyemgHCbFsb4tFfrGgDaiYAJay+IxleRVeqa/d+hjNy0ME+crRjK2o79CIBHb16+nDVS2kPj1mLEtK8NAooF6ioZzpUC8Hlau8OY0oni/xViEhMzcGYg3+R46WEobOad6vZ0BDg2qv7OsNL7qYvoz9Hx3Q9xGwhViQVyHwFemuwFTf95kvrv7cFGEdPcg2EDDpE6WUxbDV6pS6v9r5SIbG/k3jTdupSRPiE/x17Qj4x30UFW2FU8sFgSmr+zo9mKfDUwHO42OLr4yOhGehGjBg6UBZrxaFouZ9lR2rX0pLE8HKSGn/jbq9ViySv3FwaHlqBaykeNPWmO1ofLhOvdJrwB3mmZ812zvykdpZIubkbqTlqAq7NdStbLDwA9+3CZ8dsSFICFb2TN/rfl547AOvJgdi6+4inXL2DizVweeliuhZckyLLGA/wBjDvZvw3MXII4dYqVBqIyYnBRgcClTCRDMVGntIKB++ZnGT57w6xU0ZHqm/h+3ci7/1EHM6D7gXj39wyfFfCEe5wfUFgwfd7NoCFVe5aft3qelYyg7Rq51DD8HKqrlCeDqY5dkq7fJywMh5AiMLInMqFiYYsY3/yVjZjeygGEDUjFR1IO9S68jDpMc8GiekD6MhCiDcd8i9nW17NPswtsIdaBQxMXXvhPom2TRRVxdlvX+gaUbGgEz2h8aPNbXeUdBInQL9/A9Zjc65uovoDn/n38Vsov/cKN2kUpCARRRmdNfPZU8iP/d64lJEvlvtldKw/INqHVaFnF//LrCdZnJ95WraN2p005jdnvQvL/y9OeNnEg9OZAAkcwS47kg==", "b072d66eb573a00a");
//        System.out.println("解密后的字串是：[" + DeString + "]");
//        lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("解密耗时：" + lUseTime + "毫秒");


        //rsa
//        Map<String, Object> key=  RsaUtils.genKeyPair();
//        String publicKey=RsaUtils.getPublicKey(key);
//        String source = "这是一行测试RSA数字签名的无意义文字";
//        System.out.println("publicKey：\r\n" + publicKey);
//
//        System.out.println("原文字：\r\n" + source);
//        byte[] data = source.getBytes();
//        String base64EncodedData = RsaUtils.encryptByPublicKey(source, publicKey);
        //System.out.println("加密后BASE64：\r\n" + base64EncodedData);
//        System.out.println("加密后BASE64：\r\n" + base64EncodedData);


//        String privateKey=RsaUtils.getPrivateKey(key);

//        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI+pgPK9J33EF5RSsRb4yuUTQ8+8LROucssoLAS1mvA6i/7QMIXaR662YwXPjATZDR6T6XUlAYyU5yTlItNcnw2u6GndDm5ceqUd9ONHiOSIgDgvzYJPw8zSsppp+FIaJ8p92wr18YhwK+k+AcB9b7fvF7x3IvA9H5rCkU4A7GWBAgMBAAECgYBJhkHFecNtXV+H/K4/iIMmrC5ncfJMAVsk2GF2447xZbViayB6UNWg+B1Dyw3pKUDKpn60AoWBcm+99TtWmy+XJir6Tkj/qgBKc7mtOA1ismR4uO5eS9LroA+Oq2Ogl6xRqW2nz65vl7Kdx/bOrmWguzJ8tqBXC5YHhOpGMgxnFQJBANrYzwiqB/5K/w1alIi5Z1mcuIkrlLHaqxhN3G/3OBKGKy6b6Bt4pgv+j5ZPHwxc5GgEdU8bQCWiw1gRslHwiZ8CQQCoDSDWlJOHdINIncfhzPjiS7YTKFtW54KfSBtxp+ydS/X0fo9ajt8QAzUys/3uHwmHT2welKV9p7JmKD0XlPzfAkEAprbFON92iWSOWZjoUCOVNLHr7irx8m8XKcOeqvjUkV/+I1qHfe3x6G866RFLILrfENJcny+uNT2BvGR2yG8KFwJAfooEJA+UVvbAtDk6fSgecCeaXiqRPIrurIlbQOW1IWxFM2jpAMN7606/DALEPj1Yq25+dzhutINFre62sb8Z+QJBAMyQitoF8rfOqBvl7jv/guTbyKg3t+Z/oiG2oCKdHEZ8pqQP0aHr3yB0e9NwHe1JXsyoKHE/Oy5ZjbKOXMpbv58=";
//
//        System.out.println("privateKey：\r\n" + privateKey);
//
//        System.out.println("解密后：\r\n" + RsaUtils.decryptByPrivateKey("YxVuMuoPN8Oz3iQ/yok8kZWgOrTfs0BB4RlktoSgul00CXfkG2RO0hCV/Spp5KdIq2d3ZBq02vWWJ83o/7FdOcCcRFbGmyOHhoQ6u1tVF1QA41r5UiCdkZLxJPf8gMiCJ+czWbGKzqE527zdQfrf3WMm4viSxL0ogxo4HBoq3XU=", privateKey));

    }

}
