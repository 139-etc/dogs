package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.user.service.UserService;

@Controller
@RequestMapping("/admin")
public class G10Controller {
	
	@Autowired
	private UserService userService;

    /** ユーザ編集画面を表示 */
    @GetMapping("/G10")
    public String getInit(Model model,HttpSession session) {
    	
        return "/admin/G10";
    }
    
    /** ユーザ情報を更新してG03画面に遷移 */
    @PostMapping("/G10")
    public String transG03(Model model,HttpSession session) {
    	
    	
    	
    	return new G03Controller().getInit(model, session);
    }
    
    
}
