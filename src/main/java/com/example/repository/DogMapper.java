package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.domain.dog.model.Breed;
import com.example.domain.dog.model.Choice;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.model.Result;
import com.example.domain.dog.model.Status;

@Mapper
public interface DogMapper {
	public List<Breed> selectAllBreed();

	public List<Event> selectAllEvent(List<String> ids);
	
	public List<Choice>selectChoice(String incident);
	
	public void setStatus(String breed,String userId);
	
	public void updateStatus(String choiceId,String userId);
	
	public Status selectStatus(String userId);
	
	public void insertLog(String userId,String choiceId);
	
	public void insertStatus(String userId);
	
	public void countTimes(String userId) ;
	
	public void setResult(String userId);
	
	public List<Result> getResult(String userId);
}
