package com.eccsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eccsm.model.Role;

@Repository
public interface IRole extends JpaRepository<Role, Long>
{

	
}


