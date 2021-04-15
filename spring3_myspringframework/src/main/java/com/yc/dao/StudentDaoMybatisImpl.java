package com.yc.dao;

import java.util.Random;

/**
 * @program: testspring
 * @description:
 * @author: LIN
 * @create: 2021~04~04 14:43
 */

//@MyRepository         //比compent多了异常转化的功能         异常转化：从Exception 转为RuntimeException
public class StudentDaoMybatisImpl implements StudentDao{
    @Override
    public int add(String name) {
        System.out.println("Mybatis 添加学生:"+name);
        return new Random().nextInt();
    }

    @Override
    public void update(String name) {
        System.out.println("Mybatis 更新学生:"+name);

    }
}
