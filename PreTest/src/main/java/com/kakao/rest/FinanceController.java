package com.kakao.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.constant.BankCode;
import com.kakao.deeplearning.LinearRegression;
import com.kakao.model.Finance;
import com.kakao.service.FinanceService;

@RequestMapping("/finance")
@RestController
public class FinanceController {
	@Autowired
	FinanceService fs;
	
	@PostMapping("/save")
	public String saveData() {
		JSONObject object = new JSONObject();
		
		try {
			fs.bulkInsert("C:/사전과제3.csv");
			object.put("message", "정상적으로 등록되었습니다");
		} catch (Exception e) {
			object.put("message", "등록 중 오류가 발생하였습니다");
		}
		
		return object.toJSONString();
	}
	
	@GetMapping("/banklist")
	public String getBankList() {
		return fs.getBankList();
	}
	
	@GetMapping("/house/state")
	public String getHouseSupportState() {
		return fs.getHouseSupportState();
	}
	
	@GetMapping("/minmax")
	public String getMinMaxAmount() {
		BankCode bankCode = BankCode.bankNameToEnum("외환은행");
		return fs.getMinMaxAmount(bankCode.getBankId(), bankCode.getBankName());
	}
	
	@GetMapping("/predict")
	public String getPredict(@RequestBody Finance finance) {
		JSONObject object = new JSONObject();
		
		if(StringUtils.isEmpty(finance.getBank())) {
			object.put("message", "은행을 입력해주세요");
		} else if(StringUtils.isEmpty(finance.getMonth())) {
			object.put("message", "월을 입력해주세요");
		} else {
			BankCode bankCode = BankCode.bankNameToEnum(finance.getBank());
			
			List<Double> amounts = fs.getAmountsByBankId(bankCode.getBankId());
			
			double[] y = amounts.stream().mapToDouble(d -> d).toArray();
			double[] x = new double[y.length];

			for (int i = 0; i < y.length; i++) {
				x[i] = i + 1;
			}
			
			LinearRegression lr = new LinearRegression(x, y);

			double predict = lr.predict(y.length + finance.getMonth());

			object.put("bank", bankCode.name());
			object.put("year", 2018);
			object.put("month", finance.getMonth());
			object.put("amount", Math.round(predict));
		}

		return object.toJSONString();
	}
}
