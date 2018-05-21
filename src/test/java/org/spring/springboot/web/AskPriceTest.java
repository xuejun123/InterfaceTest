package org.spring.springboot.web;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiangxuejun on 2018/4/16.
 */
//@Component
@ConfigurationProperties(prefix = "connection")
public class AskPriceTest {

//    @Value("${host}")
    private String host;

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
     * 登录18800000000
     */
    @Test
    public void logIn() {
//        Properties properties = new Properties();


        Map<String,Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
//        paramMap.put("device","iphone");
//        paramMap.put("deviceId","B80A6091-DCD5-42EE-B2A0-667B6A971607");
        paramMap.put("username","18800000000");
        paramMap.put("password","123456");
        //client_secret是什么值,怎么获取,不能为空
        paramMap.put("client_secret", "HWk2m5dU6u88cdOYVqa6S0488WNSX0L9");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", host);
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
     情景一: 询价,15101079917,询价西藏林芝的众泰,应该提示"不支持对所属经销商的品牌询价"
     */
    @Test
    public void sendAskPriceTest1() {
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
        headerMap.put("Host", host);
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
        response.then().body("message", is("不支持对所属经销商的品牌询价"));
    }

    /**
     情景二: 询价,18800000000,询价西藏日喀则的众泰,应该提示"您询价的城市目前还没有该车型的经销商，您可以向邻近城市"
     */
    @Test
    public void sendAskPriceTest2() {

        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","2769");
        paramMap.put("model_id","39256");
//        paramMap.put("buyer_name","怎么了先生");
        paramMap.put("buyer_mobile","18800000000");
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
        response.then().body("data.text_0", is("您询价的城市目前还没有该车型的经销商，您可以向邻近城市"));
    }

    /**
     情景三: 询价,18800000000,询价西藏林芝的众泰,应该提示"等待报价"
     */
    @Test
    public void sendAskPriceTest3() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","2769");
        paramMap.put("model_id","39256");
//        paramMap.put("buyer_name","怎么了先生");
        paramMap.put("buyer_mobile","18800000000");
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
        response.then().body("message", is("等待报价"));
    }

    /**
     URL情况一: 不给model_id,应该提示:Incorrect parameter
     */
    @Test
    public void sendAskPriceTest4() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","2769");
//        paramMap.put("model_id","39256");
//        paramMap.put("buyer_name","怎么了先生");
        paramMap.put("buyer_mobile","18800000000");
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
        response.then().body("message", is("Incorrect parameter"));
    }

    /**
     URL情况二: 不给city_id,应该提示:Incorrect parameter
     */
    @Test
    public void sendAskPriceTest5() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","");
        paramMap.put("model_id","39256");
//        paramMap.put("buyer_name","怎么了先生");
        paramMap.put("buyer_mobile","18800000000");
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
        response.then().body("message", is("Incorrect parameter"));
    }

    /**
     URL情况三: 不给buyer_mobile,应该提示:手机号码输入有误
     */
    @Test
    public void sendAskPriceTest6() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("city_id","2769");
        paramMap.put("model_id","39256");
//        paramMap.put("buyer_name","怎么了先生");
//        paramMap.put("buyer_mobile","18800000000");
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
        response.then().body("message", is("手机号码输入有误"));
    }

}
