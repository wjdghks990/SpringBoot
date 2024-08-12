package com.githrd.demo_transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.githrd.demo_transaction.dao.ProductInMapper;
import com.githrd.demo_transaction.dao.ProductOutMapper;
import com.githrd.demo_transaction.dao.ProductRemainMapper;
import com.githrd.demo_transaction.vo.ProductVo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductInMapper productInMapper;		// 입고
	@Autowired
	ProductOutMapper productOutMapper;		// 출고
	@Autowired
	ProductRemainMapper productRemainMapper;	// 재고
	
	// 전체 조회 (입고, 출고, 재고)
	@Override
	public Map<String, List<ProductVo>> selectTotalMap() {
		
		List<ProductVo> in_list 	= productInMapper.selectList(); 		// 입고목록
		List<ProductVo> out_list 	= productOutMapper.selectList(); 	// 출고목록
		List<ProductVo> remain_list = productRemainMapper.selectList(); 	// 재고목록
		
		Map<String, List<ProductVo>> map = new HashMap<String, List<ProductVo>>();
		
		map.put("in_list", in_list);
		map.put("out_list", out_list);
		map.put("remain_list", remain_list);
		
		return map;
	}
	
	// 입고 처리
	@Override
	public int insert_in(ProductVo vo) throws Exception {
		int res = 0;
		
		// 1. 입고 등록하기
		res = productInMapper.insert(vo);
		
		// 2. 재고 등록(수정)하기
		// 2-1. 재고테이블에 상품이 등록되어있는가?
		ProductVo remainVo = productRemainMapper.selectOneFromName(vo.getName());
		// 2-2. 등록상품이 없으면...
		if(remainVo==null) {
			// 등록 추가
			res = productRemainMapper.insert(vo);
		} 
		// 2-3. 상품이 등록된 상태면... 
		else {
			// 수량 수정
			// 재고 수량 = 기존재고수량 + 추가수량
			int cnt = remainVo.getCnt() + vo.getCnt();
			remainVo.setCnt(cnt); // 재고 수량 초기화
			
			res = productRemainMapper.update(remainVo);
		}
		
		return res; // 0아니면 전부 성공
	}

	// 출고 처리
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insert_out(ProductVo vo) throws Exception {
		int res = 0;
		
		// 1. 출고 등록하기
		res = productOutMapper.insert(vo);
		
		// 2. 재고 처리(수정)하기
		// 3. 재고테이블에 상품이 등록되어있는가?
		ProductVo remainVo = productRemainMapper.selectOneFromName(vo.getName());
		// 4. 재고 목록에 상품이 없을때 ..
		// -> DB에 들어가기 전에 rollback시켜 취소시키기(트랜잭션)
		if(remainVo==null) {
			throw new Exception("remain_not");	// 예외발생시키기(작업 취소 - rollback)
		} 
		// 5. 재고 목록이 있을때..
		else {
			// 5-1. 출고가 정상 작동할 경우...
			// 재고 수량 	= 기존재고수량 + 추가수량
			int cnt		= remainVo.getCnt() - vo.getCnt();
			// 5-2. 재고수량보다 출고수량이 많은 경우...
			if(cnt < 0) {
				throw new Exception("remain_lack");
			}
			// 재고 수량 수정(정상적인 경우)
			remainVo.setCnt(cnt); 
			res = productRemainMapper.update(remainVo);		
			
		}
		
		return res; // 0아니면 전부 성공
	}

	// 입고 취소
	@Override
	public int delete_in(ProductVo vo) throws Exception {
		//				(int idx)
		int res = 0;
		//0. 취소할 입고상품 정보 얻어오기
		ProductVo inVo = productInMapper.selectOne(vo.getIdx());
		//		  vo								 (idx)
		//1. 입고상품 삭제
		productInMapper.delete(inVo.getIdx());
		//					 (idx)
		
		//2. 재고상품 수정(재고상품이 줄어들어야야 함)
		ProductVo remainVo = productRemainMapper.selectOneFromName(inVo.getName());
		//												 (vo.getName())
		int cnt = remainVo.getCnt() - inVo.getCnt();
		//								vo.getCnt();
		// 재고수량보다 입고취소수량이 많은 경우...
		if(cnt < 0) {
			throw new Exception("delete_in_lack");
		}
		
		remainVo.setCnt(cnt); // 재고 수량 초기화
		res = productRemainMapper.update(remainVo);
		
		return res;
	}

	@Override
	public int delete_out(ProductVo vo) throws Exception {
		//				 (int idx)
		int res = 0;
		//0. 취소할 출고상품 정보 얻어오기
		ProductVo outVo = productOutMapper.selectOne(vo.getIdx());	
		//		 								   (idx)
		//1. 출고상품 삭제
		productOutMapper.delete(outVo.getIdx());
		
		//2. 재고상품 수정(재고상품이 늘어나야 함)
		ProductVo remainVo = productRemainMapper.selectOneFromName(outVo.getName());
		int cnt = remainVo.getCnt() + outVo.getCnt();
		remainVo.setCnt(cnt); // 재고 수량 초기화
		res = productRemainMapper.update(remainVo);
		
		return res;
	}
}
