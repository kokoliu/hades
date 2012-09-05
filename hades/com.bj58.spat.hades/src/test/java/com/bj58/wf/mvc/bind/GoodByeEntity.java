package com.bj58.wf.mvc.bind;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class GoodByeEntity {

	
	@Min(value = 1, message = "GoodByeEntity,id 必须》0")
	private int id ; 
	
	
	@NotNull(message = "GoodByeEntity,name 不能为空！")
	private String name;
	
	@Length(min = 2, max = 6, message="GoodByeEntity.qqq 长度2-6")
	private String qqq;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getQqq() {
		return qqq;
	}


	public void setQqq(String qqq) {
		this.qqq = qqq;
	}
	
}
