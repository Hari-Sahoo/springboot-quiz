package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.models.Employee;
import com.example.repositories.EmployeeRepository;

public class EmpdetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee emp = employeeRepository.findByEmail(username);

        if (emp == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new CustomeEmp(emp);

    }

}
