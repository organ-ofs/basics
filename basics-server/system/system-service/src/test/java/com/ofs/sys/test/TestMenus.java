package com.ofs.sys.test;

import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.web.entity.SysMenus;
import com.ofs.web.base.bean.RequestTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SystemApiApplication.class})
@Slf4j
@Transactional
public class TestMenus extends TestBase {

    @Test
    @Rollback
    public void list() {
        RequestTable<SysMenus> request = new RequestTable<>();
        request.setCurrent(1);
        request.setSize(20);
        request.setData(SysMenus.builder().build());
        try {
            super.request("/system/menu/list", request)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void add() {
        SysMenus info = SysMenus.builder()
                .status("1").parentId("0").name("测试新增")
                .code("test")
                .build();
        try {
            super.request("/system/menu/add", info)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    @Rollback
    public void remove() {
        String id = "63fb073973c54df7ad5fbc8eb883b775";
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("id", id);
            super.request("/system/menu/remove", params)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    @Rollback
    public void update() {
        SysMenus info = SysMenus.builder()
                .name("修改的测试名")
                .build();
        info.setId("63fb073973c54df7ad5fbc8eb883b775");
        try {
            super.request("/system/menu/edit", info)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void get() {
        String id = "63fb073973c54df7ad5fbc8eb883b775";
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("id", id);
            super.request("/system/menu/get", params)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
