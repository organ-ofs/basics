package com.ofs.sys.test;

import com.alibaba.fastjson.JSONObject;
import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.serv.entity.SysUser;
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
public class TestUser extends TestBase {

    String id = "02b91e9de6ea4622bfbce0f1cf373baf";

    @Test
    @Rollback
    public void add() {
        SysUser info = SysUser.builder()
                .loginId("2221a")
                .status("1")
                .password("123456")
                .email("test@em.com")
                .name("test")
                .build();
        try {
            MvcResult result = super.mockMvc.perform(post("/system/user/add")
                    .header("Authorization", header)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSON(info).toString()))
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
    public void remove() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/user/remove")
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

    @Test
    @Rollback
    public void update() {
        SysUser info = SysUser.builder()
                .loginId("222a")
                .status("1")
                .password("123456")
                .email("test@em.com")
                .name("test")
                .build();
        info.setId(id);
        try {
            MvcResult result = super.mockMvc.perform(post("/system/user/edit")
                    .header("Authorization", header)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSON(info).toString()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void get() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/user/get")
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

    @Test
    public void list() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/user/list")
                    .header("Authorization", header)
                    .param("current", "1")
                    .param("size", "10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
