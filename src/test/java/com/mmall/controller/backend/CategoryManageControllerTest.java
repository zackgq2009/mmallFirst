package com.mmall.controller.backend;

import com.mmall.util.MappingUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class CategoryManageControllerTest {

    private HttpClient httpClient;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    private final String HOST = "localhost:8080";
    private final String CATEGORYMAPPING = "/manage/category/";

    private Logger logger = LoggerFactory.getLogger(CategoryManageControllerTest.class);

    @Before
    public void setUp() throws Exception {
        httpClient = HttpClientBuilder.create().build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSubCategory() {
    }

    @Test
    public void addCategory() {

    }

    @Test
    public void setCategoryName() {
    }

    @Test
    public void getCategoryId() {
        String url = MappingUtil.getMethodMapping(CategoryManageController.class, "getCategoryId");
        System.out.println(url);
        if (StringUtils.isBlank(url)) {
            logger.error("mapping error");
        } else {
            HttpUriRequest httpGet = new HttpGet("http://" + HOST + url + "?categoryId=100001");
            httpGet.setHeader("Cookie", "JSESSIONID=09EF5EBE4A1E15E4683E8861D9D4A314");

            try {
                httpResponse = httpClient.execute(httpGet);
                System.out.println(httpResponse.getStatusLine());
                String responseString = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(responseString);
                //            System.out.println(httpResponse.getEntity().getContent().toString());
            } catch (Exception e) {
                logger.error("request error", e);
            }
        }

    }
}