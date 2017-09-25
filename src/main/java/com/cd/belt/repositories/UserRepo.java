package com.cd.belt.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cd.belt.models.User;

public interface UserRepo extends CrudRepository<User, Long>{
	User findByEmail(String email);
}