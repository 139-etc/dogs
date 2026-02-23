package com.example.domain.dog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.dog.model.Breed;
import com.example.domain.dog.model.Choice;
import com.example.domain.dog.model.Event;
import com.example.domain.dog.model.Result;
import com.example.domain.dog.model.Status;
import com.example.domain.dog.service.DogService;
import com.example.repository.DogMapper;

@Service
public class DogServiceImpl implements DogService{
	
	@Autowired
	private DogMapper mapper;

	@Override
	public List<Breed> getBreeds() {
		return mapper.selectAllBreed();
	}
	
	@Override
	public List<Event> getEvent(List<String> ids){
		return mapper.selectAllEvent(ids);
	}
	
	@Override
	public List<Choice> getChoice(String incident){
		return mapper.selectChoice(incident);
	}
	
	@Override
	public void setStatus(String breed,String userId) {
		mapper.setStatus(breed,userId);
	}
	
	@Override
	public void updateStatus(String choiceId,String userId) {
		mapper.updateStatus(choiceId,userId);
	}
	
	@Override
	public Status selectStatus(String userId) {
		return mapper.selectStatus(userId);
	}
	
	@Override
	public void insertLog(String userId,String choiceId) {
		mapper.insertLog(userId, choiceId);
	}

	@Override
	public void insertStatus(String userId) {
		mapper.insertStatus(userId);
	}
	
	@Override
	public void countTimes(String userId) {
		mapper.countTimes(userId);
	}
	
	@Override
	public void setResult(String userId) {
		mapper.setResult(userId);
	}
	
	@Override
	public List<Result> getResult(String userId){
		return mapper.getResult(userId);
	}
	
	
	
}
