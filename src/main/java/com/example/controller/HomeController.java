package com.example.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.models.Employee;
import com.example.services.EmployeeService;

@Controller
public class HomeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/")
	public String login() {
		return "loginPage";
	}

	@GetMapping("/signin")
	public String signin() {
		return "loginPage";
	}
	@GetMapping("/register")
	public String register()
	{
		return "register";
	}

	@PostMapping("/saveEmp")
	public String saveEmp(@ModelAttribute Employee emp, RedirectAttributes redirectAttributes) throws IOException {
		Boolean existEmail = employeeService.existEmail(emp.getEmail());

		if (existEmail) {
			redirectAttributes.addFlashAttribute("message", "Email id already exist.");
		} else {
			Employee saveUser = employeeService.saveEmployee(emp);

			if (!ObjectUtils.isEmpty(saveUser)) {
				redirectAttributes.addFlashAttribute("message", "Save successfully");
			} else {
				redirectAttributes.addFlashAttribute("message", "Somthing wrong on server");
			}
		}

		return "redirect:register";
	}
}
