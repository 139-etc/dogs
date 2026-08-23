package com.example.controller;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.dog.service.DogService;
import com.example.domain.user.model.MUser;
import com.example.domain.user.service.UserService;
import com.example.form.G09Form;
import com.example.form.GroupOrder;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class G09Controller {
	
	// クラス名のみ（パッケージ名なし）を入手
	public String simpleName = this.getClass().getSimpleName();

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private DogService dogService;

    /** ユーザー登録画面を表示 */
    @GetMapping("/G09")
    public String getInit(Model model, Locale locale,
            @ModelAttribute G09Form form) {
        // ユーザー登録画面に遷移
        return "user/G09";
    }

    /** ユーザー登録処理 */
    @PostMapping("/G09")
    public String postSignup(Model model, Locale locale,
            @ModelAttribute @Validated(GroupOrder.class) G09Form form,
            BindingResult bindingResult) {

        // 入力チェック結果
        if (bindingResult.hasErrors()) {
            // NG:ユーザー登録画面に戻ります
            return getInit(model, locale, form);
        }

        log.info(form.toString());

        // formをMUserクラスに変換
        MUser user = modelMapper.map(form, MUser.class);

        // ユーザー登録
        userService.signup(user);
        
        //それに伴うステータスのレコードをINSERT
        dogService.insertStatus(user.getUserId());

        // ログイン画面にリダイレクト
        return "redirect:/login/G01";
    }
    
    /** ログイン画面へ遷移 */
    @PostMapping(value = "return-login")
    public String returnG01(Model model, Locale locale){
    	return "login/G01";
    }

    /** データベース関連の例外処理 */
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model) {

        // 空文字をセット
        model.addAttribute("error", "");

        // メッセージをModelに登録
        model.addAttribute("message", simpleName + "で例外が発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "G08";
    }

    /** その他の例外処理 */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {

        // 空文字をセット
        model.addAttribute("error", "");

        // メッセージをModelに登録
        model.addAttribute("message", simpleName + "で例外が発生しました");

        // HTTPのエラーコードをModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "G08";
    }
}
