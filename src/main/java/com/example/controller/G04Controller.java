package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.CommonParts;
import com.example.domain.dog.model.Result;
import com.example.domain.dog.service.DogService;

@Controller
public class G04Controller {

	@Autowired
	private DogService dogService;

	@Autowired
	private CommonParts commonParts;

	@GetMapping("/G04")
	public String getInit(Model model) {

		//ユーザ名を取得
		String userId = commonParts.getUserName();

		//ユーザ名からリザルトを取得し、モデルに詰める
		List<Result> results = dogService.getResult(userId);

		model.addAttribute("results", results);

		//G04画面に遷移
		return "game/G04";
	}
}
