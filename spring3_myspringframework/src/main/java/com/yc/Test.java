package com.yc;

import com.yc.biz.StudentBizImpl;
import com.yc.springframework.context.MyAnnotationConfigApplicationContext;
import com.yc.springframework.context.MyApplicationContext;


/**
 * @author Lenovo
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext ac=new MyAnnotationConfigApplicationContext(MyAppConfig.class);
//        Hello h= (Hello) ac.getBean("hw");
//        h.show();
        StudentBizImpl biz= (StudentBizImpl) ac.getBean("studentBizImpl");
        System.out.println(biz.getDao().toString());
    }
}
