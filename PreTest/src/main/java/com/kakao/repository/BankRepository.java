package com.kakao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kakao.model.BankInfo;

@Repository
public interface BankRepository extends JpaRepository<BankInfo, Long> {
	boolean existsByBankCode(@Param("bankCode") String bankCode);
	
	@Query(value = "SELECT \r\n" + 
			"	JSON_ARRAYAGG(JSON_OBJECT(\"bank\", bank_name))\r\n" + 
			"FROM\r\n" + 
			"	bank_info", nativeQuery = true)
	String getBankList();
}
