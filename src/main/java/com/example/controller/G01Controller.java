package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class G01Controller {

	/** ログイン画面を表示 */
	@GetMapping("/login/G01")
	public String getInit() {
		return "/login/G01";
	}
	
	@Autowired
	private JdbcTemplate jdbcTemp;
	
	@GetMapping("/db-test")
	@ResponseBody
	public String dbTest() {
		try {
			jdbcTemp.queryForObject("select 1", Integer.class);
			return "DB OK";
		} catch (Exception e) {
			e.printStackTrace();
			return "DB ERROR:" + e.getMessage();
		}
	}
	

}
