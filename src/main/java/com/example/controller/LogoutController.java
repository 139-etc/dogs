package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LogoutController {
	
	@Autowired
	HttpSession session;

    /** ログイン画面にリダイレクト */
    @PostMapping("/logout")
    public String postLogout() {
        //ログに表示
    	log.info("ログアウト");
        
    	//セッションを全削除
    	session.invalidate();
        
        return "redirect:/login/G01";
    }
}
