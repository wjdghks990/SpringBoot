package com.githrd.demo_transaction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.githrd.demo_transaction.vo.ProductVo;

@Mapper
public interface ProductInMapper {
	// 기본적인 CRUD
	
	// 전체 상품 조회
	List<ProductVo>		selectList();
	// 상품 1건 조회
	ProductVo			selectOne(int idx);	
	int					insert(ProductVo vo);
	int					update(ProductVo vo);
	int					delete(int idx);
	
}
