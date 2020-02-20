package com.ofs.sys.test;

import com.alibaba.fastjson.JSONObject;
import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.web.entity.SysResource;
import com.ofs.sys.web.message.Dict;
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
public class TestMenus extends TestBase {

    @Test
    @Rollback
    public void list() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/menu/list")
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

    @Test
    @Rollback
    public void add() {
        SysResource info = SysResource.builder()
                .type("1").status("1").parentId("0").name("测试新增")
                .code("test").type(Dict.DictEnum.MENU.getCode())
                .url("/test/add")
                .build();

        try {
            MvcResult result = super.mockMvc.perform(post("/system/menu/add")
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
        String id = "63fb073973c54df7ad5fbc8eb883b775";
        try {
            MvcResult result = super.mockMvc.perform(post("/system/menu/remove")
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
        SysResource info = SysResource.builder()
                .name("修改的测试名")
                .build();
        info.setId("63fb073973c54df7ad5fbc8eb883b775");
        try {
            MvcResult result = super.mockMvc.perform(post("/system/menu/edit")
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
    public void get() {
        String id = "63fb073973c54df7ad5fbc8eb883b775";
        try {
            MvcResult result = super.mockMvc.perform(post("/system/menu/get")
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
