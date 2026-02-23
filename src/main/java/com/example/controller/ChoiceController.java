package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.dog.model.Choice;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.model.Status;
import com.example.domain.dog.service.DogService;

@Controller
public class ChoiceController {
	
	@Autowired
	private DogService dogService;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/choice")
	public String getChoice(Model model,HttpSession session) {
		
		//セッション情報からChoice型のリストを取得し、モデルに詰める
		List<Choice> choices = (List<Choice>)session.getAttribute("choices");
		
		model.addAttribute("choices",choices);
		
		//choice画面に遷移
		return "game/choice";
	}
	
	@PostMapping(value = "/walk" , params = "choice")
	public String returnWalk(@RequestParam(required = false)String choiceId,Model model) {
		
		//前のページでなにも選ばなかったら/choiceにredirect
		if(choiceId == null) {
			return "redirect:/choice";
		}
		
		//ユーザ名を取得
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userId = authentication.getName();
    	
    	//ユーザ名と選択肢のIDを用いて差分をログに記録
    	dogService.insertLog(userId, choiceId);
		
    	//ユーザ名で指定したstatusテーブルのポイントと体力の値を選択肢のIDで操作
		dogService.updateStatus(choiceId,userId);
		
		//ユーザ名で指定したstatusテーブルから現在の体力値を取得
		Integer stamina = dogService.selectStatus(userId).getStamina();
		
		//0以下だったら失敗画面に遷移
		if(stamina <= 0) {
			dogService.setResult(userId);
			return "game/failed";
		}
		
		//乱数用の変数と入れ物を生成
    	Random random = new Random();
    	List<String> ids = new ArrayList<String>();
    	
    	//重複していない3つの選択肢を選ぶまで無限ループ
    	while(true) {
    		
    		if(ids.size() == 3) {
    			break;
    		}
    		
    		String id = String.valueOf(random.nextInt(10)+1);

    		if(!ids.contains(id)) {
    			ids.add(id);
    		}
    	}
    	
    	//3つの選択肢のIDからEvent型のListを生成し、モデルに詰める
    	List<Event> events = dogService.getEvent(ids);
    	
    	model.addAttribute("events",events);
		
    	//walk画面に戻る
		return "game/walk";
	}
	
	@PostMapping("/return")
	public String getReturn(Model model) {
		
		//ユーザ名を取得
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userId = authentication.getName();
		
    	//ユーザ名で指定したステータスを取得し、モデルに詰める
		Status status = dogService.selectStatus(userId);
		
		model.addAttribute("status",status);
		
		//リザルトを記録
		dogService.setResult(userId);

		//return画面に遷移
		return "game/return";
	}


}
