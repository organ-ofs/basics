package com.ofs.web.shiro.matcher;


import com.ofs.utils.encrypt.utils.MD5EncryptUtil;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.jwt.JwtUtil;
import com.ofs.web.utils.Tools;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * <p>
 *     Realm在验证用户身份的时候，进行密码匹配。
 * </p>
 * @author gaoly
 * @version 2017/9/25
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        Object accountCredentials = getCredentials(info);
        if (jwtToken.getPassword() != null) {
            Object tokenCredentials = MD5EncryptUtil.encrypt(String.valueOf(
                    jwtToken.getPassword()) + jwtToken.getAccount());
            if (!accountCredentials.equals(tokenCredentials)) {
                throw new DisabledAccountException("密码不正确！");
            }
        } else {
            boolean verify = JwtUtil.verify(jwtToken.getToken(), jwtToken.getAccount());
            if (!verify) {
                throw new DisabledAccountException(Tools.VERIFYFAIL);
            }
        }
        return true;
    }

}
