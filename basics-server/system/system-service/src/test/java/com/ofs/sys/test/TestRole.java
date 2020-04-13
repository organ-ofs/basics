package com.ofs.sys.test;

import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.web.entity.SysRole;
import com.ofs.web.base.bean.RequestTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SystemApiApplication.class})
@Slf4j
@Transactional
public class TestRole extends TestBase {

    String id = "abaf3cb4d0e14a249ca09b139069e6d0";

    @Test
    @Rollback
    public void add() {
        SysRole info = SysRole.builder()
                .code("admin1")
                .name("admin1")
                .description("说明")
                .build();
        try {
            super.request("/system/role/add", info)
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void update() {
        SysRole info = SysRole.builder()
                .code("admin")
                .name("admin")
                .description("更新说明")
                .build();
        info.setId(id);
        try {
            MvcResult result = super.request("/system/role/edit", info)
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void list() {
        RequestTable<SysRole> request = new RequestTable<>();
        request.setCurrent(1);
        request.setSize(20);
        request.setData(SysRole.builder().build());
        try {
            super.request("/system/role/list", request)
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Rollback
    public void delete() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/role/remove")
                    .header("Authorization", header)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback
    public void get() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/role/get")
                    .header("Authorization", header)
                    .param("id", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
