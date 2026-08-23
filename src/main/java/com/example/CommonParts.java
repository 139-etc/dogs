package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.dog.model.Breed;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.service.DogService;

@RestController
public class CommonParts {
	
	@Autowired
	private DogService dogService;
	
	//ユーザ名を取得
	public String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = authentication.getName();
		return userId;
	}
	
	
	//全犬種を取得してモデルに詰める
	public void applyBreed(Model model, DogService dogService) {
		List<Breed> breeds = dogService.getBreeds();		
		model.addAttribute("breeds",breeds);
	}
	
	//ランダムに選んだ3つのイベントをモデルに詰める
	public void applyEvents(Model model,DogService dogService) {
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
	}
	
	@PostMapping("/resetModalFlag")
	@ResponseBody
	public void resetModalFlag(HttpSession session) {
		//セッションに詰められたモーダル起動の変数をリセット
		session.removeAttribute("showErrorModal");
		
		//セッションに詰められたモーダルのエラーメッセージをリセット
		session.removeAttribute("errorMessage");
	}
	
	public void setModal(HttpSession session, List<String> errorMessage) {
		
		//モーダルのエラーメッセージがnullもしくは空でならばモーダル起動の変数を詰める
		if(errorMessage != null && errorMessage.isEmpty()) {
			//モーダル起動の変数をセッションに詰める
			session.setAttribute("showErrorModal", true);
			
			//モーダルのエラーメッセージをセッションに詰める
			session.setAttribute("errorMessage", errorMessage);
		}
	}


}
