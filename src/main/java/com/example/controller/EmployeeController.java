package com.example.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.models.Answer;
import com.example.models.EmpExam;
import com.example.models.Question;
import com.example.models.Employee;
import com.example.repositories.AnswerRepository;
import com.example.repositories.QuestionRepository;
import com.example.services.EmployeeService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/users")
public class EmployeeController {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private ExportController exportController;

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/index")
	public String IndexPage() {
		return "index";
	}

	@GetMapping("/quiz")
	public String quizPage(@RequestParam(name = "rollNo", required = false) String rollNo, Model model) {
		List<Question> questions = questionRepository.findAll();
		model.addAttribute("questions", questions);
		model.addAttribute("rollNo", rollNo);
		return "quiz";
	}

	@PostMapping("/submitAnswers")
	public String submitAnswers(@RequestParam List<String> answers, @RequestParam List<Long> questionIds,
			@RequestParam String email, RedirectAttributes redirectAttributes) {

		System.out.println("Received Question IDs: " + questionIds);
		System.out.println("Received Answers: " + answers);

		if (answers.isEmpty() || questionIds.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Error: No answers submitted.");
			return "redirect:quiz";
		}

		for (int i = 0; i < questionIds.size(); i++) {
			Answer answer = new Answer();
			answer.setQuestionId(questionIds.get(i));
			answer.setStudentAnswer(answers.get(i));
			answer.setEmail(email);
			answerRepository.save(answer);
		}

		redirectAttributes.addFlashAttribute("message", "Your answers have been submitted successfully.");
		return "redirect:quiz";
	}

	@GetMapping("/downloadAnswers")
	public void downloadAnswers(HttpServletResponse response) throws IOException {
		exportController.exportAnswers(response);
	}

	@PostMapping("/saveEmpExam")
	public String saveStudent(@RequestParam String name, @RequestParam String email, @RequestParam String Department,
			@RequestParam Date doj, RedirectAttributes redirectAttributes) {

		if (employeeService.isEmailExists(email)) {
			redirectAttributes.addFlashAttribute("message", "Email already exists in the EmpExam table!");
			return "redirect:index";
		}

		EmpExam empExam = new EmpExam();
		empExam.setName(name);
		empExam.setEmail(email);
		empExam.setDepartment(Department);
		empExam.setDoj(doj);

		employeeService.saveEmpExam(empExam);

		redirectAttributes.addAttribute("email", email);
		return "redirect:quiz";
	}

}
