package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class G01Controller {

	/** ログイン画面を表示 */
	@GetMapping("/login/G01")
	@ResponseBody
	public String getInit() {
		return "TEST";
		//return "login/G01";
	}

}
