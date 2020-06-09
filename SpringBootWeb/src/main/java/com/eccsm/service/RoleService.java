package com.eccsm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eccsm.model.Role;
import com.eccsm.repository.IRole;

@Service
@Transactional
public class RoleService {

	@Autowired
	IRole repository;


	public List<Role> getAllRoles() {
		List<Role> RoleList = repository.findAll();

		if (RoleList.size() > 0) {
			return RoleList;
		} else {
			return new ArrayList<Role>();
		}
	}

	public Role getRoleById(Long id) throws NoSuchElementException {
		Optional<Role> Role = repository.findById(id);

		if (Role.isPresent()) {
			return Role.get();
		} else {
			throw new NoSuchElementException("Role not exist for given ID");
		}
	}

	public Role createRole(Role entity) throws NoSuchElementException {

		entity = repository.save(entity);

		return entity;

	}

	public Role updateRole(long id, Role entity) throws NoSuchElementException {

		Optional<Role> Role = repository.findById(id);

		if (Role.isPresent()) {
			Role newEntity = Role.get();
			newEntity.setName(entity.getName());
			newEntity = repository.save(newEntity);

			return newEntity;
		}

		else {
			throw new NoSuchElementException("Role not exist for given ID");
		}

	}

	public void deleteRoleById(Long id) throws NoSuchElementException {
		Optional<Role> Role = repository.findById(id);

		if (Role.isPresent()) {

			repository.deleteById(id);
			 
			 }
			 

			
		 else {
			throw new NoSuchElementException("Role not exist for given ID");
		}
	}
}
