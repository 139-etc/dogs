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

import com.example.domain.dog.model.Breed;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.service.DogService;

@Controller
public class OpeningController {

	@Autowired
	private DogService dogService;
		
    @GetMapping("/opening")
    public String getOpening(Model model) {
    	
    	//全犬種を取得してモデルに詰める
    	List<Breed> breeds = dogService.getBreeds();
    	
    	model.addAttribute("breeds",breeds);
    	
    	//ユーザ名を取得
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userId = authentication.getName();
    	
    	//ユーザ名を指定した上でゲームの回数を記録
    	dogService.countTimes(userId);

        return "opening";
    }
    
    @PostMapping(value = "/walk")
    public String getWalk(@RequestParam(required = false) String breed,Model model,
    		HttpSession session) {
    	
    	//前の画面でなにも選ばなかったら自画面にredirect
    	if(breed == null) {
    		return "redirect:/opening";
    	}
    	
    	//ユーザ名を取得
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userId = authentication.getName();
    	
    	//ユーザ名で指定したstatusテーブルに犬種とそれに伴う体力値をセット
    	dogService.setStatus(breed,userId);
    	
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
    	
    	//セッション情報にも登録
    	session.setAttribute("events",events);

    	//walk画面に遷移
    	return "game/walk";
    }
}
