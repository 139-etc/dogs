package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.domain.dog.model.Result;
import com.example.domain.dog.service.DogService;

@Controller
public class ResultController {
	
	@Autowired
	private DogService dogService;
	
	@GetMapping("/result")
	public String getResult(Model model) {
		
		//ユーザ名を取得
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userId = authentication.getName();
    	
    	//ユーザ名からリザルトを取得し、モデルに詰める
    	List<Result> results = dogService.getResult(userId);
		
		model.addAttribute("results", results);
		
		//result画面に遷移
		return "game/result";
	}
}
