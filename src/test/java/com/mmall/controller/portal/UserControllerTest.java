package com.mmall.controller.portal;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.impl.UserServiceImpl;
import org.codehaus.jackson.map.util.JSONPObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
@Transactional
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-datasource.xml","classpath:generatorConfig.xml"})
public class UserControllerTest {

    private MockMvc mockMvc;
    private MockHttpSession session;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.session = new MockHttpSession();
    }

    @Test
    public void login() throws Exception{
        User user = new User();
        user.setUsername("admin");
        user.setPassword("999");
        this.mockMvc.perform(post("/user/login.do")
                .session(session)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "mockMvc")
                .header("Accept", "*/*")
                .header("Host", "localhost:8080")
                .header("cookie", "JSESSIONID=828C203528DE70A3F61F84C987EF27A0")
                .header("accept-encoding", "gzip, deflate")
                .header("content-length", "27")
                .header("Connection", "keep-alive")
                .content(JSONObject.toJSONString(user))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept("application/json;charset=UTF-8")
                )
//                .accept(MediaType.parseMediaType("application/x-www-form-urlencoded")))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print());
    }

    @Test
    public void logout() {
    }

    @Test
    public void register() {
    }

    @Test
    public void checkValid() {
    }

    @Test
    public void getUserInfo() {
    }

    @Test
    public void forgetGetQuestion() {
    }

    @Test
    public void forgetCheckAnswer() {
    }

    @Test
    public void forgetRestPassword() {
    }

    @Test
    public void resetPassword() {
    }

    @Test
    public void update_information() {
    }

    @Test
    public void get_information() {
    }
}