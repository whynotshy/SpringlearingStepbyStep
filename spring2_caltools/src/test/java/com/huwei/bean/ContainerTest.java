package com.huwei.bean;

import com.AppConfig;
import com.mimi.bean.Person;
import com.mimi.bean.PersonBmiTool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Random;

public class ContainerTest {
    private ApplicationContext ac;
    private Container c;
    private Random r;
    private PersonBmiTool pbt;

    @Before
    public void setup(){
        ac = new AnnotationConfigApplicationContext(AppConfig.class);
        c =(Container) ac.getBean("container");
        r = (Random) ac.getBean("r");
        pbt = (PersonBmiTool)ac.getBean("personBmiTool");
    }

    @Test
    public void testSave(){

        Person p1 = new Person("张三","","");
    }
}
