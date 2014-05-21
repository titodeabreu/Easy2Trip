package com.easy2trip.services;

import javax.ejb.Stateless;

import com.easy2trip.model.Member;


@Stateless
public class TestEJB implements TestRemote{

	public Member go(Member m){
		System.out.println("In the TestEJB...");
		return m;
	}
	
}
