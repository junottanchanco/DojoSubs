package com.cd.belt.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cd.belt.models.Pack;
import com.cd.belt.repositories.PackRepo;

@Service
public class PackageService {
	private PackRepo packRepo;
	
	public PackageService(PackRepo packRepo) {
		this.packRepo = packRepo;
	}
	
	public List<Pack> getAllPackages(){
		return (List<Pack>) packRepo.findAll();
	}
	
	public void addPack(Pack pack) {
		packRepo.save(pack);
	}
	
	public void changeAvailability(Long id) {
		Pack pack =packRepo.findOne(id);
		pack.setAvailable(!pack.isAvailable());
		packRepo.save(pack);
	}
	
	public Pack findByName(String name) {
		return packRepo.findByName(name);
	}
	
	public Pack findPackageById(Long id) {
		return packRepo.findOne(id);
	}
	
	public void delete(Long id) {
		packRepo.delete(id);
	}
	
}
