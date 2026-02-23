package com.example.domain.dog.model;

import lombok.Data;

@Data
public class Breed {
	private String id;
	
	private String name;
	
	private Integer stamina;

	private Integer maxEvent;
	
	private Integer maxPointEvent;

}
