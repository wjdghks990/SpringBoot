package com.githrd.demo_jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.githrd.demo_jpa.entity.Sawon;
import com.githrd.demo_jpa.repository.SawonRepository;

@RestController //  @Controller + @ResponseBody
public class SawonController {

    @Autowired  // 자동 Injection
    SawonRepository sawonRepository;

    // 전체조회
    @GetMapping("/sawons")
    public List<Sawon> selectList() {

        return sawonRepository.findAll();
    }
    
    // 전체조회+query
    @GetMapping("/sawons-dept")
    public List<Sawon> selectListWithDept() {

        return sawonRepository.findAllWithDept();
    }
    
    // 부서별조회
    @GetMapping("/sawons/{deptno}")
    public List<Sawon> selectListFromDeptno(@PathVariable int deptno) {
        return sawonRepository.findAllByDeptno(deptno);
    }    
    
    // 연봉역순정렬
    @GetMapping("/sawons-sortbysapaydesc")
    public List<Sawon> selectListSortBySapyDesc() {
        return sawonRepository.findAll(Sort.by("sapay").descending());
    }
    
    // Page처리 (Oracle 버전 11이상부터 지원!)
    @GetMapping("/sawons/{page}/{count}")
    public List<Sawon> selectListSortByPaging(@PathVariable int page, @PathVariable int count) {
        // page는 0부터 시작(0-1page, 1-2page...)
        Pageable pageAble = PageRequest.of(page,count,Sort.by("sabun"));
        Page<Sawon> pageSawon = sawonRepository.findAll(pageAble);

        // Page<Sawon> -> List<Sawon>변환 : getContent()
        return pageSawon.getContent();
    }
    
}
