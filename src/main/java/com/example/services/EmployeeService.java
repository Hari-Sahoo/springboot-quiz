package com.example.services;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.models.Employee;
import com.example.models.EmpExam;
import com.example.repositories.EmployeeRepository;
import com.example.repositories.EmpxamRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmpxamRepository empExamRepository;

	public Boolean existsByEmail(String email) {
		return employeeRepository.existsByEmail(email);
	}

	public Employee saveEmployee(Employee emp) {
		emp.setRole(emp.getRole());
		String encodePassword = passwordEncoder.encode(emp.getPassword());
		emp.setPassword(encodePassword);
		Employee saveEmp = employeeRepository.save(emp);
		return saveEmp;
	}

	public Boolean existEmail(String email) {
		return employeeRepository.existsByEmail(email);
	}

	public boolean isEmailExists(String email) {
		return empExamRepository.existsByEmail(email);
	}

	public void saveEmpExam(EmpExam empExam) {
		empExamRepository.save(empExam);
	}

	public String decodePassword(String encodedPassword) {
		try {
			return new String(Base64.getDecoder().decode(encodedPassword));
		} catch (Exception e) {
			return "Error Decoding"; // If decoding fails
		}
	}
}
