package com.example.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.models.Employee;
import com.example.repositories.EmployeeRepository;
import com.example.services.EmployeeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    EmployeeService employeeService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");
        Employee empdata = employeeRepository.findByEmail(email);
        Boolean findEmail = employeeService.existsByEmail(email);

        if (!findEmail) {
            super.setDefaultFailureUrl("/signin?error=email_not_exist");
            super.onAuthenticationFailure(request, response, exception);
            return;
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
