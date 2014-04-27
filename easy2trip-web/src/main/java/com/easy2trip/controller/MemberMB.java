package com.easy2trip.controller;

import javax.faces.bean.ManagedBean;

import com.easy2trip.model.Member;

@ManagedBean(name="memberMB")
public class MemberMB {
	
	public Member member = new Member();
	
	public String register(){
		System.out.println("foifofoi");
		return null;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
	
	

}
