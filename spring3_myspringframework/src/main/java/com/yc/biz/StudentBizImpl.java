package com.yc.biz;

import com.yc.dao.StudentDao;
import com.yc.springframework.stereotype.MyResource;
import com.yc.springframework.stereotype.MyService;

@MyService
public class StudentBizImpl {
    private StudentDao dao;

    public StudentBizImpl(StudentDao dao) {
        this.dao = dao;
    }

    public StudentBizImpl() {
    }

    public StudentDao getDao() {
        return dao;
    }


//    @MyAutowire
    @MyResource(name = "studentDaoJpaImpl")
    public void setDao(StudentDao dao) {
        this.dao = dao;
    }


    public int add(String name) {
        System.out.println("================业务层===============");
        System.out.println("用户名是否重名");
        int result=dao.add(name);
        System.out.println("================业务操作结束===============");
        return result;
    }

    public void update(String name) {
        System.out.println("================业务层===============");
        System.out.println("用户名是否重名");
        dao.update(name);
        System.out.println("================业务操作结束===============");
    }
}
