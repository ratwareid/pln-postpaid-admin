package com.ratwareid.webapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "bulan")
public class Bulan {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	private String nama;
	
}
