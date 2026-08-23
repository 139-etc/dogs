package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class G01Controller {

	/** ログイン画面を表示 */
	@GetMapping("/login/G01")
	public String getInit() {
		return "login/G01";
	}

}
