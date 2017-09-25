package com.cd.belt.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cd.belt.repositories.RoleRepo;
import com.cd.belt.models.Role;

@Service
public class RoleService {
	private RoleRepo roleRepo;
	
	public RoleService(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	public List<Role> findByName(String name) {
		return roleRepo.findByName(name);
	}
}
