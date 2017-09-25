package com.cd.belt.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cd.belt.models.Pack;

public interface PackRepo extends CrudRepository<Pack, Long>{
	Pack findByName(String name);
}
