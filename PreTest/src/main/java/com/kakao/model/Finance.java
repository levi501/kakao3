package com.kakao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Finance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	Integer year;
	
	Integer month;
	
	Long bankId;

	Integer amount;
	
	@Transient
	String bank;
	
	@OneToOne
	@JoinColumn(name = "bankId", insertable=false, updatable=false)
	private BankInfo bankInfo;
}
