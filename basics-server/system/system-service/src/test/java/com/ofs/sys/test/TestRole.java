package com.ofs.sys.test;

import com.alibaba.fastjson.JSONObject;
import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.serv.entity.SysRole;
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
        SysRole role = SysRole.builder()
                .code("admin1")
                .name("admin1")
                .description("说明")
                .build();
        role.setCreateDate("");
        role.setCreateUser("");
        try {
            MvcResult result = super.mockMvc.perform(post("/system/role/add")
                    .header("Authorization", header)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSON(role).toString()))
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
    public void update() {
        SysRole role = SysRole.builder()
                .code("admin")
                .name("admin")
                .description("更新说明")
                .build();
        role.setId(id);
        try {
            MvcResult result = super.mockMvc.perform(post("/system/role/edit")
                    .header("Authorization", header)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSON(role).toString()))
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
    public void list() {
        SysRole role = SysRole.builder().build();
        try {
            MvcResult result = super.mockMvc.perform(post("/system/role/list")
                    .header("Authorization", header)
                    .param("current", "1")
                    .param("size", "10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSONString(role)))
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
