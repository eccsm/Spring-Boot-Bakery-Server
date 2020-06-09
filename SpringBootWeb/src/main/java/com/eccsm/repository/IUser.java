package com.eccsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eccsm.model.User;

@Repository
public interface IUser extends JpaRepository<User, Long>,IUserCustom
{

	
}


