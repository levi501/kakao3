package com.kakao.constant;

public enum BankCode {
	CITY(2L, "주택도시기금"),
	KB(3L, "국민은행"),
	WOORI(4L, "우리은행"),
	SHINHAN(5L, "신한은행"),
	CITI(6L, "한국시티은행"),
	HANA(7L, "하나은행"),
	NHSH(8L, "농협은행/수협은행"),
	KEB(9L, "외환은행"),
	ETC(10L, "기타은행");
	
	private Long bankId;
	
	private String bankName;

    private BankCode(Long bankId, String bankName) {
    	this.bankId = bankId;
        this.bankName = bankName;
    }
    
    public Long getBankId() {
    	return this.bankId;
    }
    
    public String getBankName() {
    	return this.bankName;
    }
    
    public static BankCode bankNameToEnum(String bankName) {
    	BankCode bankCode = null;
    	
    	switch(bankName) {
    	case "주택도시기금":
    		bankCode = BankCode.CITY;
    		break;
    	case "국민은행":
    		bankCode = BankCode.KB;
    		break;
    	case "우리은행":
    		bankCode = BankCode.WOORI;
    		break;
    	case "신한은행":
    		bankCode = BankCode.SHINHAN;
    		break;
    	case "한국시티은행":
    		bankCode = BankCode.CITI;
    		break;
    	case "하나은행":
    		bankCode = BankCode.HANA;
    		break;
    	case "농협은행/수협은행":
    		bankCode = BankCode.NHSH;
    		break;
    	case "외환은행":
    		bankCode = BankCode.KEB;
    		break;
    	case "기타은행":
    		bankCode = BankCode.ETC;
    		break;
    	}
    	
    	return bankCode;
    }
    
    public static BankCode indexToBankCode(Integer index) {
    	BankCode bankCode = null;

    	switch(index) {
    	case 2:
    		bankCode = BankCode.CITY;
    		break;
    	case 3:
    		bankCode = BankCode.KB;
    		break;
    	case 4:
    		bankCode = BankCode.WOORI;
    		break;
    	case 5:
    		bankCode = BankCode.SHINHAN;
    		break;
    	case 6:
    		bankCode = BankCode.CITI;
    		break;
    	case 7:
    		bankCode = BankCode.HANA;
    		break;
    	case 8:
    		bankCode = BankCode.NHSH;
    		break;
    	case 9:
    		bankCode = BankCode.KEB;
    		break;
    	case 10:
    		bankCode = BankCode.ETC;
    		break;
    	}
    	
    	return bankCode;
    }
}
