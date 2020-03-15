package com.kakao.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BankInfo {
	@Id
	Long id;
	
	String bankCode;
	
	String bankName;
}
