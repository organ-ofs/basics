package com.ofs.web.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ofs.web.base.component.ApplicationContextComponent;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.constant.Knowledge;
import com.ofs.web.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * @author ly
 */
@Slf4j
public class JwtUtil {
    private JwtUtil() {
    }

    /**
     * 权限
     */
    public static final String TEST_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IiIsInRlcm1pbmFsIjoiUEMiLCJleHAiOjE1ODYzMzQ1MTcsImp0aSI6IjcwZTAxMGM4YWNkNTQ1OTk5ZmUyOTE0NmM5OTE1ZmUyIn0.DvYrV5iK71A5jVGKSP1OZ_f-gFV6zXBr97zCOeJu33k";
    /**
     * 权限
     */
    public static final String DEFAULT_JWT_PARAM = "Authorization";
    /**
     * 权限中的字符串
     */
    private static final String BEARER = "Bearer";
    /**
     * 权限中的字符串
     */
    private static final String BASIC = "Basic ";
    /**
     * 过期时间12小时
     */
    private static final long EXPIRE_TIME_TEN_HOURS = 12 * 60 * 60 * 1000L;

    /**
     * 过期时间60分钟
     */
    private static final long EXPIRE_TIME_ONE_DAY = 60 * 60 * 1000L;
    /**
     * salt
     */
    private static final String JWT_SALT = "ofs%salt";
    /**
     * 用户终端
     */
    public static final String CLAIM_TERMINAL = "terminal";


    /**
     * 处理BEARER, 返回token
     *
     * @param token request中的token
     * @return
     */
    public static String getJwtToken(String token) {
        log.debug("authenticate jwt token:[{}]", token);
        if (StringUtils.isBlank(token)) {
            return token;
        }
        if (token.startsWith(BEARER)) {
            token = token.substring(7);
        }
        if (token.startsWith(BASIC)) {
            token = token.substring(6);
        }
        return token;
    }

    /**
     * 获得jwtToken中包含的ID
     *
     * @param jwtToken jwt token
     * @return
     */
    public static String getTerminal(String jwtToken) {
        return getClaim(jwtToken, CLAIM_TERMINAL);
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @param jwtToken jwt token
     * @return jwtToken中包含的信息
     */
    public static String getClaim(String jwtToken, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            log.error("[{}] decode error：", claim, e);
            return "";
        }
    }


    /**
     * 生成访问签名,60min后过期
     *
     * @param account  用户名
     * @param terminal 终端
     * @return 加密的token
     */
    public static String getRunAsAccessToken(String account, String runAs, String terminal) {
        String jwtId = UUID.randomUUID().toString().replace("-", "");
        return sign(account, runAs, jwtId, Knowledge.TerminalEnum.get(terminal), getJwtExpireTime());
    }

    /**
     * 生成访问签名
     *
     * @param account  用户名
     * @param terminal 终端
     * @return 加密的token
     */
    public static String getAccessToken(String account, String terminal) {
        String jwtId = UUID.randomUUID().toString().replace("-", "");
        return sign(account, "", jwtId, Knowledge.TerminalEnum.get(terminal), getJwtExpireTime());
    }

    /**
     * 生成刷新签名
     *
     * @param account  用户名
     * @param terminal 终端
     * @return 加密的token
     */
    public static String getRefreshToken(String account, String terminal) {
        String jwtId = UUID.randomUUID().toString().replace("-", "");
        return sign(account, "", jwtId, Knowledge.TerminalEnum.get(terminal), getJwtExpireTime());
    }

    /**
     * 生成签名
     *
     * @param account 用户名
     * @return 加密的token
     */
    private static String sign(String account, String runAS, String jwtId, Knowledge.TerminalEnum terminal, long expireTime) {
        Date date = new Date(System.currentTimeMillis() + expireTime);
        Algorithm algorithm = Algorithm.HMAC256(generalKey(account).getEncoded());
        // 附带account信息
        return JWT.create()
                .withSubject(account)
                .withExpiresAt(date)
                .withJWTId(jwtId)
                .withClaim(CLAIM_TERMINAL, terminal.getValue())
                .withIssuer(runAS)
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param jwtToken jwt token
     * @param account  登陆账户
     * @return 是否正确
     */
    public static boolean verify(String jwtToken, String account) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(generalKey(account).getEncoded());
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(jwtToken);
            return true;
        } catch (Exception exception) {
            log.error("verify:", exception);
            return false;
        }
    }

    /**
     * 获得jwtToken中包含的用户
     *
     * @param jwtToken jwt token
     * @return
     */
    public static String getAccount(String jwtToken) {
        return JWT.decode(jwtToken).getSubject();
    }

    /**
     * 获得jwtToken中包含的用户名
     *
     * @param jwtToken jwt token
     * @return
     */
    public static String getRunAsAccount(String jwtToken) {
        return JWT.decode(jwtToken).getIssuer();
    }


    /**
     * 获得jwtToken中包含的ID
     *
     * @param jwtToken jwt token
     * @return
     */
    public static String getId(String jwtToken) {
        return JWT.decode(jwtToken).getId();
    }

    /**
     * <pre>
     *  验证token是否有效
     *  true:有效  false:无效
     * </pre>
     */
    public static Boolean isDecode(String jwtToken) {
        try {
            JWT.decode(jwtToken);
            return true;
        } catch (JWTDecodeException e) {
            log.error(" decode error：", e);
            return false;
        }
    }

    public static void main(String[] arg) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bGoiLCJpc3MiOiIiLCJ0ZXJtaW5hbCI6IlBDIiwiZXhwIjoxNTQwOTE4MzA1LCJqdGkiOiJmODcwZTM5OWVlODc0OWVhOTlhM2Y5ZWUzOWE1OTYwOCJ9.Rp-SsMhRCJwyelHQYD7oFPL_LuuW3via_xnFZjv0t78";

        DecodedJWT jwt = JWT.decode(token);
        Date expiresAt = jwt.getExpiresAt();
        System.out.println("==expiresAt==" + DateUtil.dateTimeToString(expiresAt));
        System.out.println("==new Date===" + DateUtil.dateTimeToString(new Date()));

    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public static Boolean isTokenExpired(String jwtToken) {

        try {
            DecodedJWT jwt = JWT.decode(jwtToken);
            Date expiresAt = jwt.getExpiresAt();
            return expiresAt.before(new Date());
        } catch (JWTDecodeException e) {
            log.error(" decode error：", e);
            return true;
        }
    }


    /**
     * <pre>
     *  验证token是否要刷新(离过期时间小于半个小时)
     *  true:过期   false:没过期
     * </pre>
     *
     * @param jwtToken
     * @return
     */
    public static Boolean isRefreshToken(String jwtToken) {

        try {
            long current = System.currentTimeMillis();
            current += 30 * 60 * 1000;
            Date date = new Date(current);
            DecodedJWT jwt = JWT.decode(jwtToken);
            Date expiresAt = jwt.getExpiresAt();
            return expiresAt.before(date);
        } catch (Exception e) {
            log.error(" isRefreshToken error：", e);
            return false;
        }
    }


    /**
     * 由字符串生成加密key
     *
     * @param account 用户名
     */
    public static SecretKey generalKey(String account) {
        String secret = JWT_SALT;
        try {
            secret = ApplicationContextComponent.getBeanByType(FrameProperties.class).getAuth().getJwtSecret();
        } catch (Exception e) {
            log.error("取得配置文件中设置的JWT 密码，如没有设置，则了默认:", e);
        }

        byte[] encodedKey = Base64.decodeBase64(secret + account);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 取得配置文件中设置的超时时间
     */
    public static long getJwtExpireTime() {
        long expireTime = EXPIRE_TIME_ONE_DAY;
        try {
            expireTime = ApplicationContextComponent.getBeanByType(FrameProperties.class).getAuth().getJwtExpireTime();

        } catch (Exception e) {
            log.error("Token过期时间为60分钟", e);
        }

        return expireTime;
    }

    /**
     * 获得token中的指定KEY值信息
     */
    public static String get(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
