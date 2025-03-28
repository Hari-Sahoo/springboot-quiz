package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String login() {
		return "loginPage";
	}

	@GetMapping("/signin")
	public String signin() {
		return "loginPage";
	}

	// @PostMapping("/saveEmployee")
	// public String saveStudent(@ModelAttribute Employee emp, RedirectAttributes
	// redirectAttributes) {
	// employeeService.saveEmployee(emp);
	// redirectAttributes.addAttribute("empId", emp.getEmpid());
	// return "redirect:/quiz";
	// }
}
