package com.ofs.web.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@NoArgsConstructor
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    private String loginId;

    private String password;

    private String uid;

    public JwtToken(String token, String loginId, String password) {
        this.token = token;
        this.loginId = loginId;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return JwtUtil.getAccount(token);
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
