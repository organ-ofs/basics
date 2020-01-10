package com.ofs.sys.test;

import com.alibaba.fastjson.JSONObject;
import com.ofs.sys.SystemApiApplication;
import com.ofs.sys.serv.entity.SysResource;
import com.ofs.sys.serv.message.Dict;
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
public class TestResource extends TestBase {

    String id = "127de9fad0504be7836b9288f196c3b9";

    @Test
    @Rollback
    public void list() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/resource/list")
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
    public void getTree() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/resource/getTree")
                    .header("Authorization", header)
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
    public void getTreeRole() {
        try {
            MvcResult result = super.mockMvc.perform(post("/system/resource/getTreeByRole")
                    .header("Authorization", header)
                    .param("roleId", "111"))
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
                .type(Dict.DictEnum.BUTTON.getCode()).status("1").parentId("1").name("测试新增按钮")
                .url("/test/url").code("testcode").permission("test:add").description("测试数据")
                .build();

        try {
            MvcResult result = super.mockMvc.perform(post("/system/resource/add")
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
            MvcResult result = super.mockMvc.perform(post("/system/resource/remove")
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
    public void update() {
        SysResource info = SysResource.builder()
                .type(Dict.DictEnum.BUTTON.getCode()).status("1").parentId("0").name("测试修改")
                .url("/test/url").code("testCode").permission("test:add").description("测试数据")
                .build();
        info.setId("15c3f95b94f14e9aae79c1e2254bae0c");
        try {
            MvcResult result = super.mockMvc.perform(post("/system/resource/edit")
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
        try {
            MvcResult result = super.mockMvc.perform(post("/system/resource/get")
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
