package com.example.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.models.Answer;
import com.example.models.Question;
import com.example.models.Employee;
import com.example.repositories.AnswerRepository;
import com.example.repositories.EmployeeRepository;
import com.example.repositories.QuestionRepository;
import com.example.services.EmployeeService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ExportController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/exportAnswers")
    public void exportAnswers(HttpServletResponse response) throws IOException {
        List<Question> questions = questionRepository.findAll();
        List<Answer> answers = answerRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Answers");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Question ID");
        headerRow.createCell(1).setCellValue("Question");
        headerRow.createCell(2).setCellValue("Answer");
        headerRow.createCell(3).setCellValue("Email");

        int rowNum = 1;
        for (Answer answer : answers) {
            Row row = sheet.createRow(rowNum++);
            String questionText = questions.stream().filter(q -> q.getId().equals(answer.getQuestionId())).findFirst()
                    .map(Question::getQuestionText).orElse("Question not found");
            row.createCell(0).setCellValue(answer.getId());
            row.createCell(1).setCellValue(answer.getQuestionId());
            row.createCell(2).setCellValue(questionText);
            row.createCell(3).setCellValue(answer.getStudentAnswer());
            row.createCell(4).setCellValue(answer.getEmail());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=quiz_answers.xlsx");

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        }
        workbook.close();
    }

    // Student Export

    @GetMapping("/exportEmployees")
    public void exportEmployees(HttpServletResponse response) throws IOException {
        List<Employee> employees = employeeRepository.findAll();
        if (employees == null || employees.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT, "No employees found");
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Employee ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Emp ID");
        headerRow.createCell(4).setCellValue("Role");
        headerRow.createCell(5).setCellValue("Department");
        headerRow.createCell(6).setCellValue("Password");

        int rowNum = 1;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getName());
            row.createCell(2).setCellValue(employee.getEmail());
            row.createCell(3).setCellValue(employee.getEmpid());
            row.createCell(4).setCellValue(employee.getRole());
            row.createCell(5).setCellValue(employee.getDepartment());
            row.createCell(6).setCellValue(employee.getPassword()); // Decoding

        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        }
        workbook.close();
    }
}