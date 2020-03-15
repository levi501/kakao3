package com.kakao.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.constant.BankCode;
import com.kakao.model.BankInfo;
import com.kakao.model.Finance;
import com.kakao.repository.BankRepository;
import com.kakao.repository.FinanceRepository;

@Service
public class FinanceService {
	@Autowired
	private BankRepository br;
	
	@Autowired
	private FinanceRepository fr;
	
	@Transactional
	public void bulkInsert(String filename) throws IOException {
		fr.truncateFinance();
		
		try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
				CSVParser csvParser = new CSVParser(reader,
//						CSVFormat.DEFAULT.withFirstRecordAsHeader().withAllowMissingColumnNames().withTrim());
						CSVFormat.DEFAULT.withSkipHeaderRecord(false));
				) {
			for (CSVRecord csvRecord : csvParser) {
				if(csvRecord.getRecordNumber() == 1) {
					insertBank(csvRecord);
				} else {
					insertData(csvRecord);
				}
			}
		}
	}
	
	private void insertBank(CSVRecord csvRecord) {
		for (int i = 2; i < 11; i++) {
			String bankName = csvRecord.get(i).replaceAll("\\d|\\(|\\)|억원", "");

			BankCode bankCode = BankCode.bankNameToEnum(bankName);
			
			if(bankCode != null) {
				if(!br.existsByBankCode(bankCode.name())) {
					BankInfo bank = new BankInfo();
					bank.setId(bankCode.getBankId());
					bank.setBankCode(bankCode.name());
					bank.setBankName(bankCode.getBankName());
					br.save(bank);
				}
			}
		}
	}
	
	private void insertData(CSVRecord csvRecord) {
		for (int i = 2; i < 11; i++) {
			Finance finance = new Finance();
			finance.setYear(Integer.parseInt(csvRecord.get(0))); // 연도
			finance.setMonth(Integer.parseInt(csvRecord.get(1))); // 월
			
			BankCode bankCode = BankCode.indexToBankCode(i);

			finance.setBankId(bankCode.getBankId());
			finance.setAmount(Integer.parseInt(csvRecord.get(i).replace(",", "")));
			
			fr.save(finance);
		}
	}
	
	public String getBankList() {
		return br.getBankList();
	}
	
	public String getHouseSupportState() {
		return fr.getHouseSupportState();
	}
	
	public String getMaxAmountBank() {
		return fr.getMaxAmountBank();
	}
	
	public String getMinMaxAmount(Long bankId, String bankName) {
		return fr.getMinMaxAmount(bankId, bankName);
	}
	
	public List<Double> getAmountsByBankId(Long bankId) {
		return fr.getAmountsByBankId(bankId);
	}
}
