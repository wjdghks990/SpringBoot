package com.globalIT.demo_mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.globalIT.demo_mvc.dao.TestDao;

@Service("test_service")
public class TestServiceImpl implements TestService {

    @Autowired
    @Qualifier("test_dao") // bean id가 test_dao인 객체정보 Injection+
    TestDao test_dao;

    @Autowired
    @Qualifier("test_dao2") // bean id가 test_dao인 객체정보 Injection+
    TestDao test_dao2;

    // 생성자 자동생성 : ctor 입력 + tab 2번
    public TestServiceImpl() {
        super();
        System.out.println("--TestServiceImpl()--");
    }

    // Code Generator : Ctrl + .
    @Override
    public void test() {
        
    }

    @Override
    public String hello() {

        StringBuffer sb = new StringBuffer();
        sb.append("msg1=>");
        sb.append(test_dao.hello());
        sb.append("<br>");
        sb.append("msg2=>");
        sb.append(test_dao2.hello());

        return sb.toString();
    }

}
