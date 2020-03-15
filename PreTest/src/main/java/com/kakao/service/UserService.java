package com.kakao.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kakao.model.User;
import com.kakao.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {
	@Autowired
	private UserRepository ur;
	
	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	private String secretKey = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
	
	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return ur.save(user);
	}
	
	public boolean existUser(String userid) {
		return ur.existsByUserid(userid);
	}
	
	public boolean checkSignin(User reqUser) {
		User user = ur.findByUserid(reqUser.getUserid());
		
		if(passwordEncoder.matches(reqUser.getPassword(), user.getPassword())) {
			return true;
		}
		
		return false;
	}

	public String makeJwt(String userid) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 20); // 만료 시간을 20분으로 설정
		
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        Map<String, Object> header = new HashMap<String, Object>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        
        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put("userid", userid);
		
		String jwt = Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.setExpiration(expireTime)
				.signWith(signingKey, 
				signatureAlgorithm)
				.compact();
		
		return jwt;
	}
	
	public Claims checkJwt(String jwt) throws Exception {
		Claims claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.build().parseClaimsJws(jwt).getBody();
		
		return claims;
    }
	
	public String refresh(String jwt) throws Exception {
		Claims claims = checkJwt(jwt);

		return makeJwt(claims.get("userid").toString());
	}
}
