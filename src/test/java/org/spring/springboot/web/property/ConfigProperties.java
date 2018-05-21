package org.spring.springboot.web.property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by jiangxuejun on 2018/4/17.
 */
@Component
@ConfigurationProperties
public class ConfigProperties {

    public Properties loadPropertie(String propertiesName) {
        ClassLoader classLoader = getClass().getClassLoader();
        Properties properties = new Properties();
        try {
            String path = classLoader.getResource(propertiesName).toURI().getPath();
            File file = new File(path);
            FileInputStream fileInput = new FileInputStream(file);
//            properties.load(fileInput);
            properties.load(new InputStreamReader(fileInput,"utf-8"));
//            Log4jUtil.log("- - - - - - 加载Properties文件 ： " + propertiesName + "- - - - - - ");
            if (properties.isEmpty()) {
//                Log4jUtil.log("加载文件内容为空!");
                    System.out.println("加载文件内容为空!");
            } else {
                for (String key : properties.stringPropertyNames()) {
//                    String propertyValue = getProperty(properties, key);
//                    Log4jUtil.log(key + " : " + propertyValue);
                    System.out.println(key + ":" + properties.getProperty(key));
                }
            }
//            Log4jUtil.log("- - - - - - 加载Properties文件完成 - - - - - - \n");
            System.out.println("- - - - - - 加载Properties文件完成 - - - - - - \n");
        } catch (Exception e) {
//            Log4jUtil.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return properties;
    }
}
