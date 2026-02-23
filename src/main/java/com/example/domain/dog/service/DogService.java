package com.example.domain.dog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.domain.dog.model.Breed;
import com.example.domain.dog.model.Choice;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.model.Result;
import com.example.domain.dog.model.Status;

@Service
public interface DogService {
	
	//全犬種取得
	public List<Breed> getBreeds();
	
	//イベントを取得
	public List<Event> getEvent(List<String> ids);
	
	//選択肢を取得
	public List<Choice> getChoice(String incident);
	
	//初期ステータスをセット
	public void setStatus(String breed,String userId);
	
	//ステータスを更新
	public void updateStatus(String choiceId,String userId);
	
	//ステータス情報を取得
	public Status selectStatus(String userId);
	
	//ログを記録
	public void insertLog(String userId,String choiceId);
	
	//ユーザ登録と同時にそれに関連するステータスも作成
	public void insertStatus(String userId);
	
	//ゲーム挑戦回数をステータスに記録
	public void countTimes(String userId) ;
	
	//リザルトを記録
	public void setResult(String userId);
	
	//リザルトを取得
	public List<Result> getResult(String userId);



}
