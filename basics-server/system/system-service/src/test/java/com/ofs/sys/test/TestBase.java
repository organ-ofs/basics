package com.ofs.sys.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestBase {


    String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IiIsInRlcm1pbmFsIjoiUEMiLCJleHAiOjE1ODYzMzQ1MTcsImp0aSI6IjcwZTAxMGM4YWNkNTQ1OTk5ZmUyOTE0NmM5OTE1ZmUyIn0.DvYrV5iK71A5jVGKSP1OZ_f-gFV6zXBr97zCOeJu33k";
    public MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    public ResultActions request(String url, Object info) throws Exception {
        return this.mockMvc.perform(post(url)
                .header("Authorization", header)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSON(info).toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print());
    }

    public ResultActions request(String url, MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(post(url)
                .header("Authorization", header)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print());
    }
}
