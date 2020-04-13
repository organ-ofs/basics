package com.ofs.sys.test;

import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.web.entity.SysUser;
import com.ofs.web.base.bean.RequestTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SystemApiApplication.class})
@Slf4j
@Transactional
public class TestUser extends TestBase {

    String id = "02b91e9de6ea4622bfbce0f1cf373baf";

    @Test
    @Rollback
    public void add() {
        SysUser info = SysUser.builder()
                .account("2221a")
                .status("1")
                .password("123456")
                .email("test@em.com")
                .name("test")
                .build();
        try {
            MvcResult result = super.request("/system/user/add", info)
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void remove() {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("id", id);
            MvcResult result = super.request("/system/user/remove", params)
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void update() {
        SysUser info = SysUser.builder()
                .account("222a")
                .status("1")
                .password("123456")
                .email("test@em.com")
                .name("test")
                .build();
        info.setId(id);
        try {
            MvcResult result = super.request("/system/user/edit", info)
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void get() {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("id", id);
            MvcResult result = super.request("/system/user/get", params)
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void list() {
        RequestTable<SysUser> request = new RequestTable<>();
        request.setCurrent(1);
        request.setSize(20);
        request.setData(SysUser.builder().build());
        try {
            super.request("/system/user/list", request)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
