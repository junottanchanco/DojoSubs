package com.cd.belt.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.cd.belt.models.Pack;
import com.cd.belt.services.PackageService;

@Component
public class PackageValidator {
	
	private PackageService packageService;
	
	public PackageValidator(PackageService packageService) {
		this.packageService = packageService;
	}
	
	public boolean supports(Class<?> clazz) {
		return Pack.class.equals(clazz);
	}
	
	public void validate(Object object, Errors errors) {
		Pack pack = (Pack) object;
		
		if(packageService.findByName(pack.getName()) != null) {
			errors.rejectValue("name", "Taken");
		}
	}
}
