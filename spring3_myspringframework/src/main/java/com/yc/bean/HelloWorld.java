package com.yc.bean;

import com.yc.springframework.stereotype.MyBean;
import com.yc.springframework.stereotype.MyComponent;
import com.yc.springframework.stereotype.MyPostConstruct;
import com.yc.springframework.stereotype.MyPreDestroy;

@MyComponent
public class HelloWorld {

    @MyPostConstruct
    public void setup(){
        System.out.println("MyPostConstruct");
    }
    @MyPreDestroy
    public void destyoy(){System.out.println("hello world 构造");}

    public void show(){
        System.out.println("show");
    }
    @MyBean
    public HelloWorld hw(){     //method.invoke( MyAppConfig对象 )
        return new HelloWorld();
    }
}
