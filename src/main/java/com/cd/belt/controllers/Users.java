package com.cd.belt.controllers;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cd.belt.models.Pack;
//import com.cd.belt.models.Role;
import com.cd.belt.models.User;
import com.cd.belt.services.PackageService;
//import com.cd.belt.services.RoleService;
import com.cd.belt.services.UserService;
import com.cd.belt.validator.UserValidator;

@Controller
public class Users {
	private UserService userService;
	private UserValidator userValidator;
	private PackageService packageService;
//	private RoleService roleService;
	
	public Users(UserService userService, UserValidator userValidator, PackageService packageService) {
		this.userService = userService;
		this.userValidator = userValidator;
		this.packageService = packageService;
//		this.roleService = roleService;
	}
	
	@RequestMapping("/login")
	public String login(@Valid @ModelAttribute("user") User user, @RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required=false) String logout, Model model) {
		if(error != null) {
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successfull!");
        }
		return "loginReg.jsp";
	}
	
	@PostMapping("/registration")
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
		userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "loginReg.jsp";
        }
        
        List<User> users = userService.findAll();
        int count = 0;
        for(User _user: users) {
        	if(_user.checkIfAdmin()) {
        		count++;
        	}
        }
        if(users.size() == 0) { //if there are no existing users
        	userService.saveUserWithAdminRole(user); // the 1st person to register will have ROLE_ADMIN
        }else if(count > 0) { //if users exist, everyone else will be ROLE_USER
        	userService.saveWithUserRole(user);
        }
        return "redirect:/login"; 
    }
	
	@RequestMapping(value = {"/", "/home"})
	public String home(Principal principal, Model model) {
		
		String email = principal.getName(); //gets info by email of currentUser in String
		User user = userService.findByEmail(email);
		userService.updateUserDate(user.getId(), user); // last login
		model.addAttribute("currentUser",  user);// to grab information about currentUser
		model.addAttribute("packages",  packageService.getAllPackages());// to grab information about all packages
		if(user.checkIfAdmin()) {
			return "redirect:/admin";// if logged-in is ROLE_ADMIN, then you will see admin dashboard
		}
		return "select.jsp";// if not, select package page will render
	}
	
	@RequestMapping("/dash")
	public String dash(Principal principal, Model model) {
		String email = principal.getName();
//		User user = userService.findByEmail(email);
//		userService.updateUserDate(user.getId(), user);
		model.addAttribute("currentUser",  userService.findByEmail(email));// info of currentUser will be displayed on user dashboard
		return "dashboard.jsp";
	}
	
	@RequestMapping("/admin")
	public String adminPage(Principal principal, Model model, @ModelAttribute("pack") Pack pack) { //never forget modelAttribute
		String email = principal.getName();
		model.addAttribute("currentUser", userService.findByEmail(email)); //currentUser information
		model.addAttribute("users", userService.findAll()); //all users information
		model.addAttribute("packages", packageService.getAllPackages()); //all packages information
		return "adminPage.jsp";// all models information will be displayed on this page
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) { //always use @PathVariable when you want to use specific ID
		userService.deleteUserById(id); // created in userService
		return "redirect:/admin";
	}
	
	@RequestMapping("/makeAdmin/{id}")
	public String makeAdmin(@PathVariable("id") Long id) { //always use @PathVariable when you want to use specific ID
		User user = userService.findUserById(id); // created in userService
		userService.updateUserWithAdminRole(user); // created in userService
		return "redirect:/admin";
	}
	
	@RequestMapping("/makeUser/{id}")
	public String makeUser(@PathVariable("id") Long id) { //always use @PathVariable when you want to use specific ID
		User user = userService.findUserById(id); // created in userService
		userService.updateUserWithUserRole(user); // created in userService
		return "redirect:/admin";
	}
	
	@PostMapping("/users/{id}")
	public String addPackage(@PathVariable("id") Long id, @RequestParam("pack") Long packId, @RequestParam("due") int dueDate, Principal principal, Model model) {
		//name="pack", name="due"
		
		//I DONT UNDERSTAND THESE DATES!
		// To get last day of the month
        Calendar lastDayOfMonth = Calendar.getInstance();
        lastDayOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        lastDayOfMonth.add(Calendar.MONTH, 1);
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        lastDayOfMonth.add(Calendar.DATE, -1);
        // To get date for today
        Calendar todayDate = Calendar.getInstance();
        todayDate.set(Calendar.HOUR_OF_DAY, 0);
        // To get date for next month
        Calendar nextMonth = Calendar.getInstance(); 
        nextMonth.add(Calendar.MONTH, 1);
        nextMonth.set(Calendar.DAY_OF_MONTH, dueDate);
        nextMonth.set(Calendar.HOUR_OF_DAY, 0);
        // To get date for this month
        Calendar thisMonth = Calendar.getInstance(); 
        thisMonth.set(Calendar.DAY_OF_MONTH, dueDate);
        thisMonth.set(Calendar.HOUR_OF_DAY, 0);
        
        String email = principal.getName(); //needed for userService to work
        User user = userService.findByEmail(email);
        Pack pack = packageService.findPackageById(packId); //what ID do you need to use?
        user.setPack(pack);
        
        if(dueDate > lastDayOfMonth.get(Calendar.DAY_OF_MONTH)) {
        	user.setDueDate(new Date(lastDayOfMonth.getTimeInMillis()));
        }else if(dueDate < todayDate.get(Calendar.DAY_OF_MONTH)) {
        	user.setDueDate(new Date(nextMonth.getTimeInMillis()));        	
        }else {
        	user.setDueDate(new Date(thisMonth.getTimeInMillis()));
        }
        userService.update(user);
        return "redirect:/dash";
	}
}
