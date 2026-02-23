package com.example.domain.dog.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Result {
	
	private String id;
	
	private String userId;
	
	private String userName;
	
	private String breed;
	
	private Integer stamina;
	
	private Integer point;
	
	private Integer times;
	
	private Timestamp resultTime;
}
