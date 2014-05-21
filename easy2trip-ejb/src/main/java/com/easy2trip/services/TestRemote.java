package com.easy2trip.services;

import javax.ejb.Remote;

import com.easy2trip.model.Member;

@Remote
public interface TestRemote {
	
	public Member go(Member m);

}
