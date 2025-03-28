package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

//	List<Answer> findByRollNo(int rollNo);
	

}
