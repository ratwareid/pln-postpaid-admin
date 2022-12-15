package com.ratwareid.webapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "level")
public class Level {
	@Id
	@Column(name="level_id")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	private String name;
	
}
