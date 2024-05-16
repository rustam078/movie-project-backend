package com.abc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class MoviesList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String moviename;
	
	@Column(length = 100)
	private String actor;
	private int year;
	private int rating;
	private String movietype;
	private String language;
	private String imagename;
	
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	@JsonIgnore
	private byte[] image;
	
	
}
