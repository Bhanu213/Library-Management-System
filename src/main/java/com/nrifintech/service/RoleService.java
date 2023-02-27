package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nrifintech.model.Role;
import com.nrifintech.repository.RoleRepository;

public class RoleService {
	@Autowired
	RoleRepository roleRepo;

	public List<Role> showall() {
		List<Role> roles = new ArrayList<Role>();
		for (Role r : roleRepo.findAll()) {
			roles.add(r);
		}

		return roles;

	}
	public int getRoleIdService(String roleName)
	{
		for(Role r: roleRepo.findAll())
		{
			if(roleName.equalsIgnoreCase(r.getName()))
				return r.getRoleId();
		}
		return -1;
		
	}

}
