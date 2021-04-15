package com.huwei.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  //IOC
public class Container {

    public static final int LENGTH = 5;  //定义常量

    private Object[] objs = new Object[LENGTH];
    private int count;  //计数器，计量objs数组实际存的已测量对象的数目
    private Object max; //数组中最大值的对象
    private Object min; //数组中最小值的对象
    private double avg; //平均值
    private double sum; //总值

    private Measurable measurable;  //用于测量的测量工具

    private  double objvalue;



    //设置当前容器所使用的测量工具
    @Autowired
    public void setMeasurable(Measurable measurable){
        this.measurable = measurable;
        //因为测量工具已经更换，所以数据要清零
        objs = new Object[LENGTH];
        count = 0;
        max = null;
        min = null;
        avg = 0;
    }
    public Object getMax(){return this.max;}

    public Object getMin(){return this.min;}

    public Object getAvg(){return this.avg;}

    public void save(Object obj){

    }
}
