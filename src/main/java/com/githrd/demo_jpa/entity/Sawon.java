package com.githrd.demo_jpa.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sawon") // 생략 가능(대소문자 안맞춰도 됨-테이블명과 같다면)
public class Sawon {

    @Id
    int     sabun;

    String  saname;
    String  sasex;
    String  sajob;
    String  sahire;
    
    @Column(nullable = true) // null 허용
    Integer samgr;           // -> 기본형 사용 불가
    
    int     sapay;
   
    @Column(insertable=false, updatable=false)
    int     deptno; // 참조키는 추가와 수정을 할 수 없게끔 적용(다른 테이블에도 영향을 끼칠 수 있기 때문)

    @OneToOne   // Dept - Sawon(1:1)
    @JoinColumn(name = "deptno",referencedColumnName = "deptno")
    Dept dept;

    @OneToMany // Sawon - Gogek(1:n)
    @JoinColumn(name = "godam", referencedColumnName = "sabun")
    List<Gogek> goList;

}
