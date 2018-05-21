package org.spring.springboot.web;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

import static org.junit.Assert.assertEquals;
import static io.restassured.path.xml.XmlPath.*;
import static org.junit.Assert.assertThat;

/**
 * Created by jiangxuejun on 2018/4/13.
 */
public class SoSoTest {
    @Before
    public void setUp() throws Exception {

        //定义所有请求使用ssl配置
        RestAssured.useRelaxedHTTPSValidation();
        //设置请求参数进行url code编码/设置请求、超时时间
        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                .defaultCharsetForContentType("utf-8", "application/x-www-form-urlencoded"))
                .httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", 2000)
                        .setParam("http.socket.timeout", 2000));
    }

    /**
     * 登录15101079917
     */
    @Test
    public void logInTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
//        paramMap.put("device","iphone");
//        paramMap.put("deviceId","B80A6091-DCD5-42EE-B2A0-667B6A971607");
        paramMap.put("username","15101079917");
        paramMap.put("password","123456");
        //client_secret是什么值,怎么获取,不能为空
        paramMap.put("client_secret", "HWk2m5dU6u88cdOYVqa6S0488WNSX0L9");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");
        System.out.println("log in the user");
        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/users/login/");

        response.print();
        response.getStatusCode();
    }

    /**
     * 获取排行页的信息
     */
    @Test
    public void rankGetListTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
//        paramMap.put("series_id","4532");
//        paramMap.put("device_token","");
//        paramMap.put("device","iphone");
        paramMap.put("page", 1);
        paramMap.put("type", 57);
        paramMap.put("cat", 0);
        paramMap.put("month", "2018-02");
//        paramMap.put("size",1);

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");


        Response response = given().log().all()
                .contentType("multipart/form-data")
                .multiPart("boundary", "BSLY0kbekrfkOUDvxy9cizSrunDp1aHZhcxRaM8DuzdwAazZ8kSSM_6eBSJ4m1D.opiAwB")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/rank/get-list");

        response.print();
        response.getStatusCode();

        response.then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
        response.then().body("data[0].month", is("2018年2月"));
        response.then().body("result", equalTo(true));
    }

    /**
     * 获取车系页中的口碑信息
     */
    @Test
    public void reputationDetailTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
//        paramMap.put("device","iphone");
        paramMap.put("series_id", "5149");


        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/cars/reputation-detail");

        response.print();
        response.getStatusCode();
    }

    /**
     * 获取车系页中的视频信息
     */
    @Test
    public void videoListTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
//        paramMap.put("device","iphone");
        paramMap.put("series_id", "5149");
        paramMap.put("page",1);
        paramMap.put("limit", 20);


        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/cars/video-list/");

        response.print();
        response.getStatusCode();
    }

    /**
     * 切换城市到林芝
     */
    @Test
    public void switchCityTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","C93lq4sl4k962pm8vQ4LBrvu4BkG5vamGpAt0JFv");
        paramMap.put("city_id","2");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/users/city/");

        response.print();
        response.getStatusCode();
    }

    /**
     * 获取车系页
     */
    @Test
    public void getSeriesTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","2807");
        paramMap.put("series_id","5244");
//        paramMap.put("device","iphone");
//        paramMap.put("device_token","6954ea5b5dab1bdde9c92aeb195ac836c4a873d4c08428ae812e474f979e0639");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/cars/get-series/");

        response.print();
        response.getStatusCode();
    }

    /**
     询价,18800000000,询价西藏林芝的众泰
     */

    /**
    @Test
    public void sendAskPriceTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","2807");
        paramMap.put("model_id","39256");
//        paramMap.put("buyer_name","怎么了先生");
        paramMap.put("buyer_mobile","15101079917");
        paramMap.put("extra_selections","");
//        paramMap.put("device","iphone");
//        paramMap.put("device_token","6954ea5b5dab1bdde9c92aeb195ac836c4a873d4c08428ae812e474f979e0639");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/ask-prices/send-ask-price/");

        response.print();
        response.getStatusCode();
    }
     */
    /**
     * 登录15101079917,然后退出登录
     */
    @Test
    public void logInAndLogOutTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
//        paramMap.put("device","iphone");
//        paramMap.put("deviceId","B80A6091-DCD5-42EE-B2A0-667B6A971607");
        paramMap.put("username","15101079917");
        paramMap.put("password","123456");
        //client_secret是什么值,怎么获取,不能为空
        paramMap.put("client_secret", "HWk2m5dU6u88cdOYVqa6S0488WNSX0L9");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("appid", "sosocar.topdeals");
        System.out.println("log in the user");
        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/users/login/");

        response.print();
        response.getStatusCode();
        //测试退出登录
        //获取登录后的access_token
        String accessToken = response.jsonPath().getString("data.token.access_token");
        System.out.println(accessToken);
        System.out.println("log out the user");
        Map<String,Object> logOutHeaderMap = new HashMap<String, Object>();
        logOutHeaderMap.put("Host", "api.sosocar.cn");
        logOutHeaderMap.put("Accept", "application/vnd.sosocar.v6.9+json");
        logOutHeaderMap.put("Accept-Language", "zh-cn");
        logOutHeaderMap.put("appid", "sosocar.topdeals");

        Response logOutResponse = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(logOutHeaderMap)
                .param("access_token",accessToken)
                .post("http://api.sosocar.cn/topdeals/users/logout/");

        logOutResponse.print();
        logOutResponse.getStatusCode();
    }

}
