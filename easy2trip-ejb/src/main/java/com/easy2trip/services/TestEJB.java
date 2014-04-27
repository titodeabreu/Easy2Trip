package com.easy2trip.services;

import javax.ejb.Stateless;

import com.easy2trip.model.Member;


@Stateless
public class TestEJB implements TestRemoteLocal{

	public Member go(Member m){
		
		return m;
	}
}
