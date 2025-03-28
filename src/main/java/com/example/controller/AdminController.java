package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.models.Employee;
import com.example.models.Question;
import com.example.repositories.AnswerRepository;
import com.example.repositories.QuestionRepository;
import com.example.services.EmployeeService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	QuestionRepository questionRepository;
	private static final String UPLOAD_DIR = "uploads";

	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	EmployeeService employeeService;

	@GetMapping("/adminDashboard")
	public String adminPage(Model model) {
		List<Question> questions = questionRepository.findAll();
		model.addAttribute("questions", questions);
		return "adminDashboard";
	}

	@PostMapping("/uploadQuestions")
	public String uploadQuestions(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:adminDashboard";
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Question question = new Question();
				question.setQuestionText(line);
				questionRepository.save(question);
			}
			redirectAttributes.addFlashAttribute("message", "Questions uploaded successfully!");
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Failed to upload questions.");
		}

		return "redirect:adminDashboard";
	}

	@GetMapping("/deleteQuestion")
	public String deleteQuestion(@RequestParam("id") Long questionId, RedirectAttributes redirectAttributes) {
		try {
			questionRepository.deleteById(questionId);
			redirectAttributes.addFlashAttribute("message", "Question deleted successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Failed to delete question: " + e.getMessage());
		}
		return "redirect:adminDashboard";
	}

	@GetMapping("/deleteFile")
	public String deleteFile(@RequestParam("filename") String filename, RedirectAttributes redirectAttributes) {
		try {
			Path filePath = Paths.get(UPLOAD_DIR, filename);
			File fileToDelete = filePath.toFile();
			if (fileToDelete.exists()) {
				if (fileToDelete.delete()) {
					redirectAttributes.addFlashAttribute("message", "File deleted successfully: " + filename);
				} else {
					redirectAttributes.addFlashAttribute("message", "Failed to delete file: " + filename);
				}
			} else {
				redirectAttributes.addFlashAttribute("message", "File not found: " + filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Error deleting file: " + e.getMessage());
		}
		return "redirect:adminDashboard";
	}

	@PostMapping("/deleteAllAnswers")
	public String deleteAllAnswers(RedirectAttributes redirectAttributes) {
		answerRepository.deleteAll();
		redirectAttributes.addFlashAttribute("message", "All answers have been deleted successfully.");
		return "redirect:adminDashboard"; // Redirect to the admin panel
	}

	@PostMapping("/saveEmployee")
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

		return "redirect:adminDashboard";
	}
}
