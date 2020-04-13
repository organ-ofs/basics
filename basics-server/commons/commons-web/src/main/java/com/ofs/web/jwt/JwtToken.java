package com.ofs.web.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@NoArgsConstructor
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    private String account;

    private String password;

    private String uid;
    /**
     * 终端
     */
    private String terminal;
    private String address;

    public JwtToken(String token, String account, String password, String address, String terminal, String uid) {
        this.token = token;
        this.account = account;
        this.password = password;
        this.address = address;
        this.terminal = terminal;
        this.uid = uid;
    }

    // 获取“用户”
    @Override
    public Object getPrincipal() {
        return JwtUtil.getAccount(token);
    }


    // 获取“密码”
    @Override
    public Object getCredentials() {
        return this.getPassword();
    }
}
