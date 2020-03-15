package com.kakao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kakao.model.Finance;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {
	@Modifying
    @Query(value = "TRUNCATE TABLE finance", nativeQuery = true)
    void truncateFinance();
	
	@Query(value = "SELECT\r\n" + 
			"	JSON_OBJECT(\"name\", \"주택금융 공급현황\", \"state\", JSON_ARRAYAGG(year_amount))\r\n" + 
			"FROM\r\n" + 
			"	(SELECT\r\n" + 
			"		JSON_OBJECT(\"year\", year, \"total_amount\", SUM(amount), \r\n" + 
			"        \"detail_amount\", JSON_ARRAYAGG(JSON_OBJECT(bank_info.bank_name, amount))) AS year_amount\r\n" + 
			"	FROM\r\n" + 
			"		(SELECT \r\n" + 
			"			year, \r\n" + 
			"			bank_id, \r\n" + 
			"            SUM(amount) AS amount\r\n" + 
			"		FROM \r\n" + 
			"			finance\r\n" + 
			"		GROUP BY\r\n" + 
			"			year,\r\n" + 
			"            bank_id\r\n" + 
			"		) detail_amount, bank_info\r\n" + 
			"	WHERE\r\n" + 
			"		detail_amount.bank_id = bank_info.id\r\n" + 
			"	GROUP BY year\r\n" + 
			"    ) total_amount", nativeQuery = true)
	String getHouseSupportState();
	
	@Query(value = "SELECT\r\n" + 
			"	JSON_OBJECT(\"year\", year,\r\n" + 
			"	\"bank\", bank.bank_name)\r\n" + 
			"FROM\r\n" + 
			"	finance, bank_info\r\n" + 
			"WHERE\r\n" + 
			"	finance.bank_id = bank_info.id\r\n" + 
			"GROUP BY\r\n" + 
			"	finance.year,\r\n" + 
			"	finance.bank_id\r\n" + 
			"ORDER BY\r\n" + 
			"	SUM(finance.amount) DESC\r\n" + 
			"LIMIT 1", nativeQuery = true)
	String getMaxAmountBank();
	
	@Query(value = "SELECT \r\n" + 
			"	JSON_OBJECT(\"name\", :bankName,\r\n" + 
			"    \"support_amount\", JSON_ARRAYAGG(JSON_OBJECT(year, avg_amount)))\r\n" + 
			"FROM \r\n" + 
			"	((SELECT\r\n" + 
			"		year,\r\n" + 
			"        AVG(amount) AS avg_amount\r\n" + 
			"	FROM\r\n" + 
			"		finance\r\n" + 
			"    WHERE\r\n" + 
			"		bank_id = :bankId\r\n" + 
			"    GROUP BY\r\n" + 
			"		year,\r\n" + 
			"        bank_id\r\n" + 
			"	ORDER BY\r\n" + 
			"		AVG(amount) ASC LIMIT 1)\r\n" + 
			"	UNION ALL\r\n" + 
			"	(SELECT\r\n" + 
			"		year,\r\n" + 
			"        AVG(amount) AS avg_amount\r\n" + 
			"	FROM\r\n" + 
			"		finance\r\n" + 
			"	WHERE\r\n" + 
			"		bank_id = :bankId\r\n" + 
			"	GROUP BY\r\n" + 
			"		year,\r\n" + 
			"        bank_id\r\n" + 
			"	ORDER BY\r\n" + 
			"		AVG(amount) DESC LIMIT 1)) min_max", nativeQuery = true)
	String getMinMaxAmount(@Param("bankId") Long bankId, @Param("bankName") String bankName);
	
	
	
	
	@Query(value = "SELECT amount FROM finance WHERE bank_id = :bankId", nativeQuery = true)
	List<Double> getAmountsByBankId(@Param("bankId") Long bankId);
}
