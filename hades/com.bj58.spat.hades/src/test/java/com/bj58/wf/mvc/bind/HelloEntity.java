package com.bj58.wf.mvc.bind;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class HelloEntity {

	@Min(value = 1, message = "HelloEntity,id 必须》0")
	private int id ;
	
	@NotNull(message = "HelloEntity,name 不能为空！")
	private String name ;
	
	private String pass ;

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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
