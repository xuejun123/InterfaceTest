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
 * Created by jiangxuejun on 2018/4/17.
 */
public class CarInsuranceTest {
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
    public void getZhongAnInsuranceTest() {
        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headerMap.put("Accept-Language", "zh-cn");
//        headerMap.put("apptoken", "tk-expert/6.9(6f133592e925553c5112f03cf5d91be234942237c4fd8ccc0f0690229121d500f5249bb9cc6fc3790213effd8688f2a9b6cb2e7e5703f11ab9f20d74b5c00248),tk-expert/6.9(6f133592e925553c5112f03cf5d91be234942237c4fd8ccc0f0690229121d500f5249bb9cc6fc3790213effd8688f2a9b6cb2e7e5703f11ab9f20d74b5c00248)");
//        headerMap.put("Upgrade-Insecure-Requests", "1");
//        headerMap.put("Cookie", "_session_id=6865953f59868d06cc4a111abd336377815818be");
        headerMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216");

        Response response = given().log().all()
//                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .get("http://sosocar.cn/waptopdeals/zhongan-insurance");

        response.print();
        response.getStatusCode();

    }

    @Test
    public void zhonganRegionTest() {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("region_id","466");

        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Host", "api.sosocar.cn");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("Origin", "http://sosocar.cn");
        headerMap.put("Referer", "http://sosocar.cn/waptopdeals/zhongan-insurance");
        headerMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216");

        Response response = given().log().all()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .headers(headerMap)
                .params(paramMap)
                .post("http://sosocar.cn/waptopdeals/zhongan-region");

        response.print();
        response.getStatusCode();
        response.then().body("result", is(true));
    }
}
