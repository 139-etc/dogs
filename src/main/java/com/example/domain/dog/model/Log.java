package com.example.domain.dog.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Log {
	
	private String id;
	
	private String breed;
	
	private Integer stamina;
	
	private Integer point;
	
	private Timestamp gameStartTime;
}
