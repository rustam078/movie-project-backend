package com.abc.dto;

import lombok.Data;

@Data
public class SearchDto {
	private String actor;
	private int year;
	private int rating;
	private String movietype;
	private String language;
}
