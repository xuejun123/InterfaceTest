package org.spring.springboot.web;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.springboot.Application;
import org.spring.springboot.web.property.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
/**
 * Created by jiangxuejun on 2018/4/17.
 */
public class SecondCar {

    ConfigProperties propertiesObj = new ConfigProperties();
    Properties properties = propertiesObj.loadPropertie("application.properties");
    String host = properties.get("header.host").toString();
    String accept = properties.get("header.accept").toString();
    String acceptLanguage = properties.get("header.accept_language").toString();
    String acceptId = properties.get("header.appid").toString();
    String user1_accessToken = properties.get("user1.access_token").toString();
    String user1_phone = properties.get("user1.phone").toString();
    String user1_owerName = properties.get("user1.owner_name").toString();

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

    @Test
    public void carInfo1() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token",user1_accessToken);
        paramMap.put("phone",user1_phone);
        paramMap.put("model_id",1134332);
        paramMap.put("city_id",1);
        paramMap.put("selling_intention","111");
        paramMap.put("month","2017-09");
        paramMap.put("owner_name",user1_owerName);
        paramMap.put("mileage",5);
//        paramMap.put("device","iphone");
//        paramMap.put("device_token","6954ea5b5dab1bdde9c92aeb195ac836c4a873d4c08428ae812e474f979e0639");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", host);
        headerMap.put("Accept", accept);
        headerMap.put("Accept-Language", acceptLanguage);
        headerMap.put("appid", acceptId);

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/second-car/car-info");

        response.print();
        response.getStatusCode();
        response.then().statusCode(200)
            .body("data.future_prices[0].month",equalTo("2018.04"))
            .body("data.future_prices.month",hasItem("2018.10"))
                //[-1]数组的最后一个元素
            .body("data.future_prices[-1].month",equalTo("2019.04"))
                //findALl 查出来的是一个数组,要用hasItem
            .body("data.future_prices.findAll {future_prices->future_prices.month == '2018.10'}.price",hasItem("14.61"))
                //find是查找某一个,用equalTo
            .body("data.future_prices.find {future_prices->future_prices.month == '2018.10'}.price",equalTo("14.61"))
                //判断数组的数量使用.size()
            .body("data.future_prices.size()",equalTo(5))

        ;

    }

    @Test
    public void carInfo2() {

        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("source_id","1523945580-138");
//        paramMap.put("device","iphone");
//        paramMap.put("device_token","6954ea5b5dab1bdde9c92aeb195ac836c4a873d4c08428ae812e474f979e0639");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", host);
        headerMap.put("Accept", accept);
        headerMap.put("Accept-Language", acceptLanguage);
        headerMap.put("appid", acceptId);

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://api.sosocar.cn/topdeals/index/statistic");

        response.print();
        response.getStatusCode();
//        response.then().body("result",is(true));
        response.then().body("result",equalTo(true));
    }

}
