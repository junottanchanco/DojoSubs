package com.cd.belt.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cd.belt.models.Pack;
import com.cd.belt.services.PackageService;
import com.cd.belt.services.UserService;
import com.cd.belt.validator.PackageValidator;

@Controller
@RequestMapping("/packages")
public class PackageController {
	private UserService userService;//access to user services
	private PackageService packageService;//access to package services
	private PackageValidator packageValidator;//access to creation of new package validations
	
	public PackageController(UserService userService, PackageService packageService, PackageValidator packageValidator){
		this.userService = userService;
		this.packageService = packageService;
		this.packageValidator = packageValidator;
	}
	
	@PostMapping("/new")
	public String addPack(@Valid @ModelAttribute("pack") Pack pack, BindingResult result, RedirectAttributes errors, Model model, Principal principal) {
		packageValidator.validate(pack, result);
		
		if(result.hasErrors()) {
			String email = principal.getName();
			model.addAttribute("users",  userService.findAll());
			model.addAttribute("currentUser", userService.findByEmail(email));
			model.addAttribute("packages", packageService.getAllPackages());
			return "adminPage.jsp";
		}else {
			packageService.addPack(pack);
		}
		return"redirect:/admin";
	}
	
	@RequestMapping("/changeAvailabilty/{id}")
	public String activate(@PathVariable("id") Long id) {
		packageService.changeAvailability(id);
		return "redirect:/admin";
	}
	
	@RequestMapping("/delPack/{id}")
	public String delPack(@PathVariable("id") Long id) {
		Pack pack = packageService.findPackageById(id);
		if(pack.getUsers().size() < 1) {
			packageService.delete(id);
		}
		return "redirect:/admin";
	}
}
