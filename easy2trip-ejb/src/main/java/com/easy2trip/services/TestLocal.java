package com.easy2trip.services;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.easy2trip.model.Member;

@Local
public interface TestLocal {
	
	public Member go(Member m);

}
