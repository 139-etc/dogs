package com.example.domain.dog.service.impl;

import java.sql.Timestamp;
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
public class DogServiceImpl implements DogService {

	@Autowired
	private DogMapper mapper;

	@Override
	public List<Breed> getBreeds() {
		return mapper.selectAllBreed();
	}

	@Override
	public List<Event> getEvent(List<String> ids) {
		return mapper.selectAllEvent(ids);
	}

	@Override
	public List<Choice> getChoice(String incident) {
		return mapper.selectChoice(incident);
	}

	@Override
	public void setStatus(String breed, String userId) {
		mapper.setStatus(breed, userId);
	}

	@Override
	public void updateStatus(String choiceId, String userId) {
		mapper.updateStatus(choiceId, userId);
	}

	@Override
	public Status selectStatus(String userId) {
		return mapper.selectStatus(userId);
	}

	@Override
	public void insertLog(String userId, String choiceId) {
		mapper.insertLog(userId, choiceId);
	}

	@Override
	public void insertStatus(String userId) {
		mapper.insertStatus(userId);
	}

	@Override
	public void updateGameStartTime(String userId) {
		mapper.updateGameStartTime(userId);
	}

	@Override
	public void setResult(String userId) {
		mapper.setResult(userId);
	}

	@Override
	public List<Result> getResult(String userId) {
		return mapper.getResult(userId);
	}

	@Override
	public void importResult(List<String> record) {
		String id = record.get(0);
		String userId = record.get(1);
		String userName = record.get(2);
		String breed = record.get(3);
		String stamina = record.get(4);
		String point = record.get(5);
		Timestamp startedTime = Timestamp.valueOf(record.get(6));
		Timestamp updateTime = Timestamp.valueOf(record.get(7));
		
		mapper.importResult(id, userId, userName, breed, stamina, point, startedTime, updateTime);
	}

	@Override
	public void deleteResult(String id) {
		mapper.deleteResult(id);
	}

	@Override
	public int checkDeplicateResult(String userId, String timestamp) {
		return mapper.checkDeplicateResult(userId, Timestamp.valueOf(timestamp));
	}

	@Override
	public void updateResult(Result result) {
		mapper.updateResult(result);
	}

}
