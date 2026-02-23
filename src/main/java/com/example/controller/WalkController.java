package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.dog.model.Choice;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.service.DogService;

@Controller
public class WalkController {
	
	@Autowired
	private DogService dogService;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/walk")
	public String getWalk(Model model,HttpSession session) {
		
		//セッション情報からEvent型のListを取得し、モデルに詰める
		List<Event> events = (List<Event>)session.getAttribute("events");

		model.addAttribute("events",events);
		
		return "game/walk";
	}
	
	@PostMapping("/choice")
	public String getChoice(@RequestParam(required = false) String incident,Model model,
			HttpSession session) {
		
		//前の画面でなにも選んでいなかったらredirect
		if(incident == null) {
			return "redirect:/walk";
		}
		
		//イベントの内容から選択肢2つのListを取得し、モデルに詰める
		List<Choice> choices = dogService.getChoice(incident);
		
		model.addAttribute("choices",choices);
		
		//セッション情報にも登録
		session.setAttribute("choices", choices);
		
		//choice画面に遷移
		return "game/choice";
	}

}
