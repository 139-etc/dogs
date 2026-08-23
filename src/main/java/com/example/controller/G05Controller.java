package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.CommonConstants;
import com.example.CommonParts;
import com.example.domain.dog.model.Choice;
import com.example.domain.dog.service.DogService;

@Controller
public class G05Controller {
	
	@Autowired
	private DogService dogService;
	
	@Autowired
	private CommonParts commonParts;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/G05")
	public String getInit(Model model,HttpSession session) {
		
		//セッションから3つのイベントを取り出してモデルに詰める
		model.addAttribute("events", session.getAttribute("events"));
		
    	//セッションからモーダル起動の変数を取り出しモデルに詰める
    	model.addAttribute("showErrorModal",session.getAttribute("showErrorModal"));
    	
    	//セッションからモーダルのエラーメッセージの変数を取り出してモデルに詰める
    	model.addAttribute("errorMessage",session.getAttribute("errorMessage"));
		
		//G05画面に遷移
		return "game/G05";
	}
		
	@PostMapping("/G06")
	public String transG06(@RequestParam(required = false) String incident,Model model,
			HttpSession session) {
		
		//前の画面でなにも選んでいなかったらredirect
		if(incident == null) {
    		//モーダル起動の変数をセッションに詰める
    		session.setAttribute("showErrorModal", true);
    		
    		//モーダルのエラーメッセージをセッションに詰める
    		List<String> errorMessage = new ArrayList<String>();
    		errorMessage.add(CommonConstants.EM_E01);
    		
    		session.setAttribute("errorMessage", errorMessage);

			return "redirect:/G05";
		} else {
    		//セッションに詰められたモーダル起動の変数をリセット
    		session.removeAttribute("showErrorModal");
		}
		
		
		//イベントの内容から選択肢2つのListを取得し、モデルに詰める
		List<Choice> choices = dogService.getChoice(incident);
		
		model.addAttribute("choices",choices);
		
		//セッション情報にも登録
		session.setAttribute("choices", choices);
		
		//G06画面に遷移
		return "game/G06";
	}

}
