package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.CommonConstants;
import com.example.domain.dog.model.Result;
import com.example.domain.dog.service.DogService;
import com.example.domain.user.model.MUser;
import com.example.domain.user.service.UserService;
import com.example.form.G03Form;

@Controller
@RequestMapping("/admin")
public class G03Controller {

	@Autowired
	private UserService userService;

	@Autowired
	private DogService dogService;

	/** アドミン権限専用画面に遷移 */
	@GetMapping("/G03")
	public String getInit(Model model, HttpSession session) {

		//入れ物となるMUserを作成
		MUser user = new MUser();
		List<MUser> users = new ArrayList<MUser>();

		//セッションの有無でモデルに詰めるための引数の中身を変える
		if (session.getAttribute("users") == null) {
			users = userService.getUsers(user);
		} else {
			users = (List<MUser>) session.getAttribute("users");
		}

		//モデルに詰める
		model.addAttribute("users", users);

		return "admin/G03";
	}

	/** 検索結果を画面に表示 */
	@PostMapping(value = "/G03", params = "users")
	public String getUsers(@Validated G03Form form, Model model, HttpSession session, BindingResult bindingResult) {

		// 入力チェック結果
		if (bindingResult.hasErrors()) {
			// NG:アドミン兼下船用画面に戻ります
			return getInit(model, session);
		}

		//FormからユーザIDとユーザ名を入手し、MUserにセット
		MUser user = new MUser();
		user.setUserId(form.getUserId());
		user.setUserName(form.getUserName());

		//Formの情報から得た検索結果をセッションに詰める
		session.setAttribute("users", userService.getUsers(user));

		return "redirect:/admin/G03";
	}

	/** ユーザ編集画面に遷移 */
	@PostMapping(value = "/G03", params = "userEdit")
	public String transG10(@RequestParam(value = "userIds", required = false) List<String> userIds, Model model,
			HttpSession session) {

		if (userIds == null || userIds.isEmpty()) {
    		//モーダル起動の変数をセッションに詰める
    		session.setAttribute("showErrorModal", true);
    		
    		//モーダルのエラーメッセージをセッションに詰める
    		List<String> errorMessage = new ArrayList<String>();
    		errorMessage.add(CommonConstants.EM_E01);
    		
    		session.setAttribute("errorMessage", errorMessage);
			
			return "redirect:/admin/G03";
		}

		//DBから引き出したユーザ情報をモデルに詰める
		model.addAttribute("users", userService.getUserMulti(userIds));

		return "/admin/G10";
	}

	/** ユーザログ画面に遷移 */
	@PostMapping(value = "/G03", params = "userLog")
	public String transG11(@RequestParam(value="userIds",required = false) List<String> userIds, Model model, HttpSession session) {

		//セッションに詰めておく
		session.setAttribute("userIds", userIds);
		
		if (userIds == null || userIds.isEmpty()) {
			
    		//モーダル起動の変数をセッションに詰める
    		session.setAttribute("showErrorModal", true);
    		
    		//モーダルのエラーメッセージをセッションに詰める
    		List<String> errorMessage = new ArrayList<String>();
    		errorMessage.add(CommonConstants.EM_E01);
    		
    		session.setAttribute("errorMessage", errorMessage);
			
			return "redirect:/admin/G03";
		}
		
		
		//複数ユーザのリザルトのための入れ物を作る
		List<Result> resultsSum = new ArrayList<Result>();
		
		//ユーザIDのリストからユーザIDを取り出す
		for(String userId : userIds) {
			//ユーザIDからリザルトのリストを取り出す
			List<Result> results = dogService.getResult(userId);
			
			//リザルトのリストをまとめる
			resultsSum.addAll(results);
		}
		
		//モデルにチェックボックスで選んだ値を詰める
		model.addAttribute("resultsSum", resultsSum);
		
		//セッションにも詰めておく
		session.setAttribute("resultsSum", resultsSum);
		session.setAttribute("userIds", userIds);

		return "redirect:/admin/G11";
	}
}
