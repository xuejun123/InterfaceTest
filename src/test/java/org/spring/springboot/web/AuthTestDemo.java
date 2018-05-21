package org.spring.springboot.web;
import io.restassured.RestAssured;
import io.restassured.config.SessionConfig;
import io.restassured.filter.session.SessionFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;

/**
 * Created by jiangxuejun on 2018/4/27.
 */

public class AuthTestDemo {
//    @BeforeClass
//    public static void proxy() {
//        RestAssured.proxy("127.0.0.1",8080);
//    }
    @Test
    public void testHttpBaseAuth() {
        given()
                /**
                http认证信息,就是需要输入用户名密码
                使用nginx建一个网站
                 docker run --rm --name http -v /Users/jiangxuejun/root/nginx/:/usr/share/nginx/html
                        -v /Users/jiangxuejun/root/nginx/:/etc/nginx/
                        -v /Users/jiangxuejun/root/nginx/:/var/log/nginx -p 9002:80 nginx
                */
                .auth().basic("hogwarts","123456")
        .when()
                .get("http://101.132.159.87:9002/")
        .then()
                .statusCode(200)
        ;
    }
    @Test
    public void testSession() {
        RestAssured.config().sessionConfig(
                new SessionConfig().sessionIdName("JSESSIONID.86912bdc"));
        //通过session filter机制,关联,比如带有生命周期的
        SessionFilter sessionFilter = new SessionFilter();

        given()
                .filter(sessionFilter)
                .queryParam("Submit", "%E7%99%BB%E5%BD%95")
                .queryParam("j_password","tester1")
                .queryParam("j_username","tester1")
                .queryParam("from","%2F")
                .queryParam("Jenkins-Crumb","d4640a14034f177684d20d1ec6cce63e")
        .when()
                .log().all()
                .post("http://47.100.105.183:8081/j_acegi_security_check")
                .prettyPeek()
        .then()
                .log().all()
//                .statusCode(302)
                ;
        given()
                //带有登录session的
                .filter(sessionFilter)
         .when().log().all()
                .get("http://47.100.105.183:8081/").prettyPeek()
          .then()
                .statusCode(200)
                ;
    }

    @Test
    public void testAuth() {
        useRelaxedHTTPSValidation();
        String token = "e77c56dcb9a8303b992f212703937d761aec2124";
        given()
                .auth().oauth2(token)
        .when()
                .get("https://api.github.com/user")
                .prettyPeek()
        .then()
                .statusCode(200)
                ;
    }
}
