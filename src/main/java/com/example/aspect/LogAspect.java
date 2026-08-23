package com.example.aspect;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {

	private static final Logger aclog = LoggerFactory.getLogger("ACLOG");
	private static final Logger errorlog = LoggerFactory.getLogger("ERRORLOG");

	@Around("execution(* com.example.controller..*(..))")
	public Object aclogMethod(ProceedingJoinPoint joinPoint) throws Throwable, Exception {

		long start = System.currentTimeMillis();

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodName = signature.toShortString();

		// 引数一覧
		Object[] args = joinPoint.getArgs();
		String argsStr = Arrays.toString(args);

		// ===== Model と Session を抽出 =====
		Model model = null;
		HttpSession session = null;

		for (Object arg : args) {
			if (arg instanceof Model) {
				model = (Model) arg;
			}
			if (arg instanceof HttpSession) {
				session = (HttpSession) arg;
			}
		}

		// ===== Model の中身 =====
		String modelContent = "-";
		if (model != null) {
			Map<String, Object> map = model.asMap();
			modelContent = map.toString();
		}

		// ===== Session の中身 =====
		String sessionContent = "-";
		if (session != null) {
			StringBuilder sb = new StringBuilder("{");
			Enumeration<String> names = session.getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				Object value = session.getAttribute(name);
				sb.append(name).append("=").append(value).append(", ");
			}
			sb.append("}");
			sessionContent = sb.toString();
		}

		// ユーザ名を抽出
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = authentication.getName();

		// START ログ
		aclog.debug("[START] {} args={} user={} model={} session={}",
				methodName, Arrays.toString(args), userId, modelContent, sessionContent);

		Object result = joinPoint.proceed();

		// END ログ
		aclog.debug("[END] {} args={} user={} model={} session={}",
				methodName, Arrays.toString(args), userId, modelContent, sessionContent);

		return result;
	}

	@Around("execution(* com.example..*(..))")
	public Object errorlogMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		// ユーザ名を抽出
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		//仮のログインユーザ名
	    String userId = "anonymous";

	    //ログインしていたらそこからログインユーザ名を取得
	    if (authentication != null && authentication.isAuthenticated()
	            && !(authentication instanceof AnonymousAuthenticationToken)) {
	        userId = authentication.getName();
	    }

		//メソッド名を抽出
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodName = signature.toShortString();

		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			errorlog.error("[ERROR] {} message={} user={}",
					methodName, e.getMessage(), userId, e);
			throw e;
		}

	}

}
