package com.jabal.SpringBootImageAdminUbloadApp.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;


import com.jabal.SpringBootImageAdminUbloadApp.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
    public Set<Role> findByName(String USER);

     
}

