package org.spring.springboot.web;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.builder.MultiPartSpecBuilder;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.path.json.JsonPath.from;
//import static jdk.nashorn.internal.parser.TokenType.XML;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

import static org.junit.Assert.assertEquals;
import static io.restassured.path.xml.XmlPath.*;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;

/**
 * Spring Boot HelloWorldController 测试 - {@link HelloWorldController}
 *
 * Created by bysocket on 16/4/26.
 */
public class DemoTest {
//    @BeforeClass
//    public static void beforeAll(){
//
//        //可以将这个filter设置成一个全局的,这样其他方法中默认就先获取这个filter了,这里写的东西可以自动带入下面的请求中
//        filters((req, res, ctx) -> {
//            System.out.println(req);
//            System.out.println(res);
//            Response resOrigin = ctx.next(req, res);
//            ResponseBuilder builder = new ResponseBuilder().clone(resOrigin);
//            Response resNew = builder.setBody("filter").build();
//            return resNew;
//        });
//    }

    @BeforeClass
    public static void setUp() throws Exception {
        //RestAssured提供了一个全面局性的变量,可以将一些值进行抽取
        RestAssured.baseURI = "https://testerhome.com";
//        RestAssured.proxy("127.0.0.1",8080);
        //定义所有请求使用ssl配置
        RestAssured.useRelaxedHTTPSValidation();
        //设置请求参数进行url code编码/设置请求、超时时间
//        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("utf-8", "application/x-www-form-urlencoded")).
//                httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", 2000).
//                        setParam("http.socket.timeout", 2000));
    }
    @Test
    public void testSayHello() {
        assertEquals("Hello,World!", new HelloWorldController().sayHello());
    }

    @Test
    public void testProxy() {
        given().log().all().proxy("127.0.0.1", 8888).get("http://www.baidu.com/s?wd=mp3").then().log().all();
    }

    @Test
    public void testJson1() {
        given().log().all()
//                .param("seen_ids","")
//                .param("count","5")
//                .param("only_unfollowed","true")
                .get("http://www.jianshu.com/users/recommended?seen_ids=&count=5&only_unfollowed=true")
                .then().log().all();
    }

    @Test()
    public void getHttpsTest2() {
        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .param("q", "自动化测试")
                .param("start", 0)
                .param("count", 2)
                .get("https://api.douban.com/v2/book/search");
        // 打印出 response 的body
        response.print();
        response.getStatusCode();
        Map cookies = response.getCookies();
        System.out.println(cookies);
    }

    @Test
    public void testJson2() {
//        curl 'https://m.douban.com/rexxar/api/v2/gallery/subject_feed?start=0&count=4&subject_id=1770782&ck=null'
//                -H 'Origin: https://book.douban.com'
//                -H 'Accept-Encoding: gzip, deflate, br'
//                -H 'Accept-Language: zh-CN,zh;q=0.9'
//                -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36'
//                -H 'Accept: */*'
//                -H 'Referer: https://book.douban.com/subject/1770782/'
//                -H 'Cookie: bid=WGuG_aFnf5w; gr_user_id=2dfcb497-3ad9-4dc9-a3eb-a6a905389cfb; _vwo_uuid_v2=2BEDB0C5FE242B6D6FB3CA45BB214DE6|2e228dd001246763ec41f2e7c2d44549; ll="118123"; __utma=30149280.1432307854.1492478534.1521377065.1522233733.6; __utmc=30149280; __utmz=30149280.1522233733.6.6.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmt=1; gr_session_id_22c937bbd8ebd703f2d8e9445f7dfd03=9e1e2026-2a72-4115-aeaf-f6f746f9fb34; gr_cs1_9e1e2026-2a72-4115-aeaf-f6f746f9fb34=user_id%3A0; __utmt_douban=1; __utmb=30149280.4.10.1522233733; viewed="1770782_26413027_25742200"'
//                -H 'Connection: keep-alive' --compressed
        given().log().all()
                .header("Origin", "https://book.douban.com")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36")
                .header("Accept", "*/*")
                .header("Referer", "https://book.douban.com/subject/1770782/")
                .header("Connection", "keep-alive")
                .param("start", "0")
                .param("count", "4")
                .param("subject_id", "1770782")
                .param("ck", "null")
                .get("https://m.douban.com/rexxar/api/v2/gallery/subject_feed")
                .then().log().all().body("subject_id", equalTo("177782"));
    }

    @Test
    public void testBargainList() {
        Response response = given().log().all()
                .header("Host", "api.sosocar.cn")
                .header("Accept", "application/vnd.sosocar.v6.9+json")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .header("idfa", "B80A6091-DCD5-42EE-B2A0-667B6A971607")
//                .header("Cookie", "_session_id=fb35af7a5f0c421568c7969a5d81b7ccd00aa3f3")
                .header("appid", "sosocar.topdeals")
                .param("access_token", "RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4")
                .post("http://api.sosocar.cn/topdeals/index/bargain-list");
        response.print();
        response.then().body("result", equalTo(true));
    }

    @Test
    public void testRankGetList() {
        Response response = given().log().all()
                .contentType("multipart/form-data")
                .multiPart("boundary", "BSLY0kbekrfkOUDvxy9cizSrunDp1aHZhcxRaM8DuzdwAazZ8kSSM_6eBSJ4m1D.opiAwB")
//                .header("X-Request-Header", startsWith("multipart/form-data"))
                .header("Host", "api.sosocar.cn")
                .header("Accept", "application/vnd.sosocar.v6.9+json")
                .header("Accept-Language", "zh-cn")
                .header("appid", "sosocar.topdeals")
                .param("access_token", "RnmfaOzMKQWg4Dd5IgkHTM1HJSFIEIIc2YC3aWk4")
                .param("device_token", "")
                .param("device","iphone")
                .param("page", 1)
                .param("size", 1)
                .param("type", 57)
                .param("cat", 0)
                .param("month", "2018-02")
                .post("http://api.sosocar.cn/topdeals/rank/get-list");

        response.print();
        response.getStatusCode();
//        response.then().assertThat().body(matchesJsonSchemaInClasspath("src/test/resources/json/products-schema.json"));
//        response.then().log().all();
//        response.getBody().prettyPrint();
//        response.then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
//        response.then().body("data[0].month", is("2018年2月"));
//        response.then().body("result", equalTo(true));
    }


    @Test
    public void jsonParsed() {
        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .params("q", "自动化测试", "start", 0, "count", 5)
                .get("https://api.douban.com/v2/book/search");
        // 打印出 response 的body
        response.print();

        int statusCode = response.getStatusCode();
        System.out.println("statusCode:" + statusCode);

        // 获取Response 的所有 headers 并输出
        Headers headers = response.getHeaders();
        System.out.println(headers.toString());

        // 获取Response中header名为Content-Type的值
        String contentType = response.getHeader("Content-Type");
        System.out.println("contentType:" + contentType);
        // 等同上面方法
        System.out.println(headers.get("Content-Type"));

        // 校验某个Header 是否存在
        System.out.println(headers.hasHeaderWithName("fasdfaf"));
        System.out.println(headers.hasHeaderWithName("Server"));

        // 如果Response 的headers不为空则返回true
        System.out.println(headers.exist());

        Map<String, String> cookiesMap = response.cookies();
        for (String key : cookiesMap.keySet()) {
            System.out.println(key + ":" + cookiesMap.get(key));
        }

        System.out.println(response.cookie("bid"));


        // 把Response 的body转成string类型
        System.out.println(response.getBody().asString());

        int count = response.jsonPath().getInt("count");
        System.out.println("count:" + count);

        // 获取所有的 subtitle
        ArrayList<String> subtitles = response.jsonPath().get("books.subtitle");
        for (int i = 0; i < subtitles.size(); i++) {
            System.out.println(subtitles.get(i));
        }

        // 获取特定某个的subtitle
        String subtitle = response.jsonPath().get("books.subtitle[0]");
        System.out.println(subtitle);

        // 获取倒数第二个的subtitle
        String subtitle1 = response.jsonPath().get("books.subtitle[-2]");
        System.out.println(subtitle1);

        // 获取特定tags底下的所有title
        ArrayList<String> tagTitle = response.jsonPath().get("books.tags[2].title");
        for (int i = 0; i < tagTitle.size(); i++) {
            System.out.println(tagTitle.get(i));
        }

        // 获取所有的 title
        ArrayList<ArrayList<String>> tagTitles = response.jsonPath().get("books.tags.title");
        for (int i = 0; i < tagTitles.size(); i++) {
            for (int j = 0; j < tagTitles.get(i).size(); j++) {
                System.out.println(tagTitles.get(i).get(j));
            }
            System.out.println("---------------------");

        }

        // 获取Response json里面所有title = "Selenium 2自动化测试实战"的title
        String title = response.jsonPath().get("books.title.findAll{title ->title==\"Selenium 2自动化测试实战\"}").toString();
        System.out.println(title);

        // 获取Response json中 1< numRaters <=20的所有 numRaters
        String numRaters = response.jsonPath().get("books.rating.numRaters.findAll{numRaters -> numRaters>1 && numRaters<=20}").toString();
        System.out.println(numRaters);

        // 获取Response json种title = "基于Selenium 2的自动化测试"对应的 author
        String title2 = response.jsonPath().get("books.findAll{it.title==\"基于Selenium 2的自动化测试\"}.author").toString();
        System.out.println(title2);

    }

    @Test
    public void testDemo1() {
        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .params("q", "自动化测试", "start", 0, "count", 5)
                .expect()
                // 判断 title是否包含了 自动化 和 自动化测试
                .body("books.tags[2].title", hasItems("自动化", "自动化测试"))
                // 判断 count 值是否为 5
                .body("count", is(5))
                // 判断 publisher 值是否为 "电子工业出版社"
                .body("books.publisher[0]", is("电子工业出版社"))
                // 判断 title 是否等于 5
                .body("count", equalTo(5))
                .when()
                .get("https://api.douban.com/v2/book/search");
        // 打印出 response 的body
        response.print();
    }

    @Test
    public void xmlPathTest1() {
        String XML = "<shopping>\n" +
                "<category type=\"groceries\">\n" +
                "<item>\n" +
                "<name>Chocolate</name>\n" +
                "<price>10</price>\n" +
                "</item>\n" +
                "<item>\n" +
                "<name>Coffee</name>\n" +
                "<price>20</price>\n" +
                "</item>\n" +
                "</category>\n" +
                "<category type=\"supplies\">\n" +
                "<item>\n" +
                "<name>Paper</name>\n" +
                "<price>5</price>\n" +
                "</item>\n" +
                "<item quantity=\"4\">\n" +
                "<name>Pens</name>\n" +
                "<price>15</price>\n" +
                "</item>\n" +
                "</category>\n" +
                "<category type=\"present\">\n" +
                "<item when=\"Aug 10\">\n" +
                "<name>Kathryn's Birthday</name>\n" +
                "<price>200</price>\n" +
                "</item>\n" +
                "</category>\n" +
                "</shopping>\n";
        String name = with(XML).get("shopping.category.item[0].name");
        System.out.println(name);
        assertThat("name diff", name, equalTo("Chocolate"));
        System.out.println(with(XML).get("**.find{ it.name =='Pens'}.price").toString());
        //python -m CGIHTTPServer 9999  启动一个网站
        String onename = when().
                get("http://127.0.0.1:9999/aa.xml").
                then()
                //使用find语法来找到某一个特定属性(特定tyoe)的值
                .body("shopping.category.find { it.@type == 'groceries' }.item.name", hasItems("Chocolate", "Coffee"))
                //下面是两种方式获取数组中某一个值
                .body("shopping.category[1].item[1].name", equalTo("Pens"))
                .body("shopping.category.item.name[2]",equalTo("Paper"))
                //直接获取某一个节点下的数量
                .body("shopping.category.size()",equalTo(3))
                //**.find,可以通过这种方式直接查找某一个值,这种方式只能用于xml,而json是不支持的
                .body("**.find { it.name == 'Pens' }.price", equalTo("15"))
                //可以将过滤出来的值直接导出给一个变量,除此之外也可以.extract().response(),这样就导出为一个response的值
            .extract().path("shopping.category.item.name[2]")
        ;
        System.out.println(onename);

    }

    @Test
    public void getTest() {

        //方式一,将参数放到一个Mpa中
//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("limit", 2);
//        map.put("offset","0");
//        map.put("type","last_actived");
//        given().params(map).get("https://testerhome.com/api/v3/topics.json").prettyPeek();

        //方式二,格式化
//        Map<String,Object> map2 = new HashMap<String, Object>();
//        map2.put("topics", "topics");
//        map2.put("topicid",12192);
//        //?limit=2&offset=0&type=last_actived
//        get("https://testerhome.com/{topics}/{topicid}",map2).prettyPrint();


        //方式三,直接放到接口后面
        get("https://testerhome.com/{topics}/{topicid}","topics",12192).prettyPrint();
    }

    @Test
    public void postTest() {
//        given().param("user[login]","test@qq.com").param("user[password]","111111").param("user[remember_me]",0)
//                .post("https://testerhome.com/account/sign_in").prettyPeek();

//        Map<String,Object> map = new HashMap<String, Object>();
//            map.put("user[login]", "test@qq.com");
//            map.put("user[password]","111111");
//            map.put("user[remember_me]",0);
//        given().params(map).post("https://testerhome.com/account/sign_in").prettyPeek();

        //传递带body的
//        given().body("{\"message\"} : \"hello world\"}").post("https://testerhome.com/api/v3/topics.json").prettyPeek();
//
//        //传递一个文件信息
        File file = new File("/user/desktop/a.txt");
//        given().body(file).post("https://testerhome.com/api/v3/topics.json").prettyPeek();
//
//        //带有cookie/header的登录
//        given().cookie("username","xxxxxx").get("https://testerhome.com/account/sign_in").prettyPeek();
//        given().header("username","xxxxxx").get("https://testerhome.com/account/sign_in").prettyPeek();
        //url编码,避免中文乱码
        given().urlEncodingEnabled(true).param("user","社区").param("pass","1111").get("https://testerhome.com/account/sign_in").prettyPeek();
        given().multiPart(file).post("https://testerhome.com/account/sign_in");
    }

    @Test
    public void jsonPathTest() {
        Response response = get("https://testerhome.com/api/v3/topics.json?limit=2&offset=0&type=last_actived");
        List<Object> list = response.jsonPath().getList("topics");
        System.out.println(list.size());
        System.out.println(response.jsonPath().getString("topics[0].id"));

    }

    @Test
    public void testJsonGlobal() {

        given().when().get("/api/v3/topics/10254.json").prettyPeek()
            .then()
            .statusCode(200)
            .body("topic.title",equalTo("优质招聘汇总"))
        ;
    }

    @Test
    public void testJsonPost() {
        HashMap<String,Object> data = new HashMap<String, Object>();
        data.put("id",6040);
        data.put("title","通过代理安装 appium");
        data.put("name","小明");

        //嵌套的HashMap
        HashMap<String,Object> root = new HashMap<String, Object>();
        root.put("topic",data);
        //rest assured提供直接用他们提供的ContentType
        given().contentType(ContentType.JSON)
                //body可以通过直接用HashMap的方式发出去
                .body(root)
        .when()
                .post("http://www.baidu.com")
                //断言接口的请求时间
                //当公司需要调用第三方接口的时候,需要去断言第三方接口的请求时间,当某一天第三方接口突然变得很慢时
                //用户不断的请求时就会堆积成队列,有可能导致自己公司的服务器崩溃了,需要提前预警
        .then().time(lessThan(1000L))
        ;
    }

    @Test
    public void multiApi() {
        Response response = given().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
//            .then().statusCode(200).extract().path("topic.user.name")
                .then().statusCode(200).extract().response()
                ;
        String name = response.path("topic.user.name");
        Integer uid = response.path("topic.user.id");
        System.out.println(name);
        given()
                .queryParam("q",name)
                .cookie("uid",uid)
                .cookie("name",name)
        .when().get("/search")
                //第二个接口使用上一个接口中的值
        .then().statusCode(200).body(containsString(name))
                ;
    }

    @Test
    public void testSpec() {
        //通用的断言可以都放在这里,可以复用这个spec
        ResponseSpecification rs = new ResponseSpecBuilder().build();
        rs.statusCode(200);
        rs.time(lessThan(2000L));
        rs.body(not(containsString("error")));

        given().get("/api/v3/topics/6040.json")
        .then().spec(rs);
    }

    @Test
    public void testFilter() {
        RestAssured.filters();
    }
}
