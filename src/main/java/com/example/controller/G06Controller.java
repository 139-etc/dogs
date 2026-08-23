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
import com.example.domain.dog.model.Status;
import com.example.domain.dog.service.DogService;

@Controller
public class G06Controller {
	
	@Autowired
	private DogService dogService;
	
	@Autowired
	private CommonParts commonParts;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/G06")
	public String getInit(Model model,HttpSession session) {
		
		//セッション情報からChoice型のリストを取得し、モデルに詰める
		List<Choice> choices = (List<Choice>)session.getAttribute("choices");
		
		model.addAttribute("choices",choices);
		
    	//セッションからモーダル起動の変数を取り出しモデルに詰める
    	model.addAttribute("showErrorModal",session.getAttribute("showErrorModal"));
		
		//G06画面に遷移
		return "/game/G06";
	}
	
	@PostMapping(value = "/G05" , params = "G06")
	public String returnG05(@RequestParam(required = false)String choiceId,Model model,HttpSession session) {
		
		//前のページでなにも選ばなかったら/G06にredirect
		if(choiceId == null) {
    		//モーダル起動の変数をセッションに詰める
    		session.setAttribute("showErrorModal", true);
    		
    		//モーダルのエラーメッセージをセッションに詰める
    		List<String> errorMessage = new ArrayList<String>();
    		errorMessage.add(CommonConstants.EM_E01);
    		
    		session.setAttribute("errorMessage", errorMessage);
    		
			return "redirect:/G06";
		} else {
    		//セッションに詰められたモーダル起動の変数をリセット
    		session.removeAttribute("showErrorModal");
		}
		
		//ユーザ名を取得
    	String userId = commonParts.getUserName();
    	
    	//ユーザ名と選択肢のIDを用いて差分をログに記録
    	dogService.insertLog(userId, choiceId);
		
    	//ユーザ名で指定したstatusテーブルのポイントと体力の値を選択肢のIDで操作
		dogService.updateStatus(choiceId,userId);
		
		//ユーザ名で指定したstatusテーブルから現在の体力値を取得
		Integer stamina = dogService.selectStatus(userId).getStamina();
		
		//0以下だったら失敗画面に遷移し、ログに残す
		if(stamina <= 0) {
			dogService.setResult(userId);
			dogService.insertLog(userId, choiceId);
			return "/game/G08";
		}
		
		//セッションから3つのイベントを取り出してモデルに詰める
		model.addAttribute("events", session.getAttribute("events"));
		
    	//セッションからモーダル起動の変数を取り出しモデルに詰める
    	model.addAttribute("showErrorModal",session.getAttribute("showErrorModal"));
		
    	//G05画面に戻る
		return "/game/G05";
	}
	
	@PostMapping("/G07")
	public String transG07(Model model) {
		
		//ユーザ名を取得
    	String userId = commonParts.getUserName();
		
    	//ユーザ名で指定したステータスを取得し、モデルに詰める
		Status status = dogService.selectStatus(userId);
		
		model.addAttribute("status",status);
		
		//リザルトを記録
		dogService.setResult(userId);

		//G07画面に遷移
		return "/game/G07";
	}


}
