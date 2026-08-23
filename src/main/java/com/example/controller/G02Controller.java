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
import com.example.domain.dog.service.DogService;

@Controller
public class G02Controller {

	@Autowired
	private DogService dogService;
	
	@Autowired
	private CommonParts commonParts;
		
    @GetMapping("/G02")
    public String getInit(Model model,HttpSession session) {
    	
    	//全犬種を取得してモデルに詰める
    	commonParts.applyBreed(model, dogService);
    	
    	//ユーザ名を取得
    	String userId = commonParts.getUserName();
    	
    	//ユーザ名を指定した上でゲームの回数を記録
    	dogService.updateGameStartTime(userId);
    	
    	//セッションからモーダル起動の変数を取り出しモデルに詰める
    	model.addAttribute("showErrorModal",session.getAttribute("showErrorModal"));

    	//セッションからモーダルのエラーメッセージを取り出しモデルに詰める
    	model.addAttribute("errorMessage",session.getAttribute("errorMessage"));

        return "/G02";
    }
    
    @PostMapping(value = "/G05")
    public String transG05(@RequestParam(required = false) String breed,Model model,
    		HttpSession session) {
    	
    	//前の画面でなにも選ばなかったら自画面にredirect
    	if(breed == null) {
    		//モーダル起動の変数をセッションに詰める
    		session.setAttribute("showErrorModal", true);
    		
    		//モーダルのエラーメッセージをセッションに詰める
    		List<String> errorMessage = new ArrayList<String>();
    		errorMessage.add(CommonConstants.EM_E01);
    		
    		session.setAttribute("errorMessage", errorMessage);
    		
    		return "redirect:/G02";
    	} else {
    		//セッションに詰められたモーダル起動の変数をリセット
    		session.removeAttribute("showErrorModal");
    	}
    	
    	//ユーザ名を取得
    	String userId = commonParts.getUserName();
    	
    	//ユーザ名で指定したstatusテーブルに犬種とそれに伴う体力値をセット
    	dogService.setStatus(breed,userId);

		//ランダムに選んだ3つのイベントをモデルに詰める
		commonParts.applyEvents(model,dogService);
    	
    	//セッション情報にも登録
    	session.setAttribute("events",model.getAttribute("events"));

    	//G05画面に遷移
    	return "game/G05";
    }
}
