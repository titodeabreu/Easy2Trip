package com.easy2trip.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.easy2trip.model.Member;
import com.easy2trip.services.TestLocal;
import com.easy2trip.services.TestRemote;

@ManagedBean(name="memberMB")
public class MemberMB {
	
	public Member member = new Member();
	
	@EJB
	public TestRemote ejbRemote;
	@EJB
	public TestLocal ejblocal;
	
	public String register(){
		ejbRemote.go(new Member());
		ejblocal.go(null);
		
		System.out.println("In the MB... 3");
		return null;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
	
	

}
