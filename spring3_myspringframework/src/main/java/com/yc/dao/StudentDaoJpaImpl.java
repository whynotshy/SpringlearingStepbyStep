package com.yc.dao;


import com.yc.springframework.stereotype.MyRepository;

import java.util.Random;

/**
 * @program: testspring
 * @description:
 * @author: LIN
 * @create: 2021~04~04 14:43
 */

@MyRepository         //比compent多了异常转化的功能         异常转化：从Exception 转为RuntimeException
public class StudentDaoJpaImpl implements StudentDao{
    @Override
    public int add(String name) {
        System.out.println("jpa 添加学生:"+name);
        return new Random().nextInt();
    }

    @Override
    public void update(String name) {
        System.out.println("jpa 更新学生:"+name);

    }
}
