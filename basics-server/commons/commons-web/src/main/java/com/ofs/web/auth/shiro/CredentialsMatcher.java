package com.ofs.web.auth.shiro;


import com.ofs.utils.encrypt.utils.MD5EncryptUtil;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.utils.JwtUtil;
import com.ofs.web.utils.Tools;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author gaoly
 * @version 2017/9/25
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        Object accountCredentials = getCredentials(info);
        if (jwtToken.getPassword() != null) {
            Object tokenCredentials = MD5EncryptUtil.encrypt(String.valueOf(
                    jwtToken.getPassword()) + jwtToken.getLoginId());
            if (!accountCredentials.equals(tokenCredentials)) {
                throw new DisabledAccountException("密码不正确！");
            }
        } else {
            boolean verify = JwtUtil.verify(jwtToken.getToken(), jwtToken.getLoginId(), accountCredentials.toString());
            if (!verify) {
                throw new DisabledAccountException(Tools.VERIFYFAIL);
            }
        }
        return true;
    }

}
