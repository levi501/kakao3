package com.kakao.rest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.model.User;
import com.kakao.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService us;
	
	@PostMapping("/signup")
	public String signup(@RequestBody User user) {
		JSONObject object = new JSONObject();
		
		if(StringUtils.isEmpty(user.getUserid())) {
			object.put("message", "아이디를 입력해주세요");
		} else if(StringUtils.isEmpty(user.getPassword())) {
			object.put("message", "비밀번호를 입력해주세요");
		} else if(us.existUser(user.getUserid())) {
			object.put("message", "이미 등록된 사용자입니다");
//		} else if(us.save(user) == null) {
//			object.put("message", "사용자 등록을 실패하였습니다");
		} else {
			object.put("message", "사용자를 등록하였습니다");
		}

		return object.toJSONString();
	}
	
	@PostMapping("/signin")
	public String signin(@RequestBody User user) throws Exception {
		JSONObject object = new JSONObject();
		
		if(StringUtils.isEmpty(user.getUserid())) {
			object.put("message", "아이디를 입력해주세요");
		} else if(StringUtils.isEmpty(user.getPassword())) {
			object.put("message", "비밀번호를 입력해주세요");
		} else if(!us.checkSignin(user)) {
			object.put("message", "로그인 정보가 일치하지 않습니다");
		} else {
			object.put("token", us.makeJwt(user.getUserid()));
		}

		return object.toJSONString();
	}
	
	@PostMapping("/refresh")
	public String refresh(@RequestHeader("authorization") String authorization) throws Exception {
		JSONObject object = new JSONObject();
		
		object.put("token", us.refresh(authorization.substring(7)));
		
		return object.toJSONString();
	}
}
