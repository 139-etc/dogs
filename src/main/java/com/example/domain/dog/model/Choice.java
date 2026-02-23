package com.example.domain.dog.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Choice {
	
	private String id;
	
	private String incident;
	
	private String choice;
	
	private Integer resultStamina;
	
	private BigDecimal resultPointRate;
	
}
