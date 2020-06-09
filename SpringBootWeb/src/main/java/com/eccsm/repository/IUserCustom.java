package com.eccsm.repository;

import com.eccsm.model.User;

public interface IUserCustom {
	
	 public User findUserByUsername(String Username);

}
