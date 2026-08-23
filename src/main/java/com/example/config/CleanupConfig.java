package com.example.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CleanupConfig implements DisposableBean {

	@Autowired
	HttpSession session;
	
	private String target = "C:\\pleiades\\2022-12\\workspace\\dog\\logs";
	
	//プロジェクト終了時処理
	@Override
	public void destroy() {
		
		//セッションを削除
		session.invalidate();
		
        try {
	            // 今日の日付を取得
	            LocalDate now = LocalDate.now();

	            // フォーマットパターンを指定（例: 2026-05-16 → 20260516）
	            DateTimeFormatter dtfm = DateTimeFormatter.ofPattern("yyyyMMdd");

	            // 日付を文字列に変換
	            String today = now.format(dtfm);
	            
	            //検索するパスを指定（例：C:\\pleiades\\2022-12\\workspace\\dog\\logs）
	            File targetPath = new File(target);
	            
            	//検索するパス配下のファイル・フォルダを取得
            	File [] objects = targetPath.listFiles();
            	
            	//フォルダを入れるための入れ物を作成
            	List<String> folders = new ArrayList<String>();

            	//ディレクトリかつ日付が入っているもののみ入れ物に追加
            	for(File object : objects) {
            		if(object.isDirectory() && object.getName().contains(today)) {
            			folders.add(object.getName());
            		}
            	}

            	//「(日付)_(日付を含んだディレクトリの数+1)」のパスを作成
            	String dateFolder = target + "\\" + today + "_" + String.valueOf(folders.size() + 1);
	            
	            //ディレクトリ作成
	            Files.createDirectory(Paths.get(dateFolder));
				
	            //Logback のコンテキストを停止（ファイルロック解除）
	            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	            context.stop();
	            
				//変数を用意
				Path sourceMove = null; //移動元
				Path targetMove = null; //移動先
				
				//logファイルの数だけ作成した先に移動
				for(File object : objects) {
					if(object.getName().contains(".log")) {
						sourceMove = Paths.get(target + "\\" + object.getName());
						targetMove = Paths.get(dateFolder + "\\" + object.getName());
						Files.move(sourceMove, targetMove, StandardCopyOption.REPLACE_EXISTING);
					}					
				}
	        } catch (IllegalArgumentException e) {
				e.printStackTrace();
	        } catch (DateTimeParseException e) {
				e.printStackTrace();
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}
}
