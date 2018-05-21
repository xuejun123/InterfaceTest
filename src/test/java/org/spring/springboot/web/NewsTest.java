package org.spring.springboot.web;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
/**
 * Created by jiangxuejun on 2018/4/16.
 */
public class NewsTest {
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
     2 => 1,//评测
     1 => 100,//新闻
     101 => 101,//导购
     102 => 102,//用车
     103 => 103,//技术
     104 => 104,//文化
     105 => 105,//新车
     106 => 106,//玩车
     107 => 107,//行情
     108 => 108,//二手车
     1. 判断一页的数量是否是20条数据
     */
    @Test
    public void NewsTest1() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("access_token","RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4");
        paramMap.put("page",1);
        paramMap.put("type",108);
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
                .post("http://api.sosocar.cn/topdeals/follow-models/news/");

        response.print();
        response.getStatusCode();
        //一页应该显示20条资讯
        ArrayList<String> datas =response.jsonPath().get("data.flows");
        System.out.println(datas.size());
        assert datas.size() == 20: "一页的数量不是20";

    }
}
