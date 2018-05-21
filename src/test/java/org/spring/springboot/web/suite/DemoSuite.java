package org.spring.springboot.web.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.spring.springboot.web.NewsTest;
import org.spring.springboot.web.SecondCar;

/**
 * Created by jiangxuejun on 2018/4/25.
 * RunWith,可以将几个测试文件先后顺序的放到一起执行,如果需要传输参数的话,需要用到parameter注解
 */

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                SecondCar.class,
                NewsTest.class
        }
)
public class DemoSuite {

}
