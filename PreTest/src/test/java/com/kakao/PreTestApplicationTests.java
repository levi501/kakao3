package com.kakao;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.kakao.model.Finance;
import com.kakao.rest.FinanceController;
import com.kakao.rest.UserController;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class PreTestApplicationTests {
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	UserController uc;
	
	@Autowired
	FinanceController fc;
	
	@Test
	void contextLoads() {
	}

	@Test
	@Order(1)
	public void signupTest() throws Exception {
		JSONObject json = new JSONObject();
		
		json.put("userid", "admin");
		json.put("password", "1234");

		webTestClient.post()
		.uri("/signup")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(json)).exchange()
        .expectStatus().isOk()
        .expectBody()
        .json("{\"message\":\"사용자를 등록하였습니다\"}");
	}

	@Test
	@Order(2)
	public void signinTest() throws Exception {
		JSONObject json = new JSONObject();
		
		json.put("userid", "admin");
		json.put("password", "1234");
		
		webTestClient.post()
		.uri("/signin")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(json.toJSONString())).exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.token").hasJsonPath();
	}
	
	@Test
	@Order(3)
	public void refreshTokenTest() throws Exception {
		JSONObject json = new JSONObject();
		
		json.put("userid", "admin");
		json.put("password", "1234");
		
		EntityExchangeResult<byte[]> result =  webTestClient.post()
		.uri("/signin")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(json.toJSONString())).exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.token").hasJsonPath()
        .returnResult();

		JSONObject object = (JSONObject) new JSONParser().parse(new String(result.getResponseBody()));
		
		if(object.get("token") != null) {
			String token = (String) object.get("token");

			JSONObject refreshToken = (JSONObject) new JSONParser().parse(uc.refresh("Bearer: " + token));
			
			assertNotNull(refreshToken.get("token"));
		}
	}
	
	@Test
	@Order(4)
	public void saveDataTest() throws Exception {
		String result = fc.saveData();

		assertEquals(result, "{\"message\":\"정상적으로 등록되었습니다\"}");	
	}
	
	@Test
	@Order(5)
	public void bankListTest() throws Exception {
		String result = fc.getBankList();

		assertEquals(result, "[{\"bank\": \"주택도시기금\"}, {\"bank\": \"국민은행\"}, {\"bank\": \"우리은행\"}, {\"bank\": \"신한은행\"}, {\"bank\": \"한국시티은행\"}, {\"bank\": \"하나은행\"}, {\"bank\": \"농협은행/수협은행\"}, {\"bank\": \"외환은행\"}, {\"bank\": \"기타은행\"}]");	
	}
	
	@Test
	@Order(6)
	public void houseSupportStateTest() throws Exception {
		String result = fc.getHouseSupportState();

		assertEquals(result, "{\"name\": \"주택금융 공급현황\", \"state\": [{\"year\": 2005, \"total_amount\": 48016, \"detail_amount\": [{\"주택도시기금\": 22247}, {\"국민은행\": 13231}, {\"우리은행\": 2303}, {\"신한은행\": 1815}, {\"한국시티은행\": 704}, {\"하나은행\": 3122}, {\"농협은행/수협은행\": 1486}, {\"외환은행\": 1732}, {\"기타은행\": 1376}]}, {\"year\": 2006, \"total_amount\": 41210, \"detail_amount\": [{\"주택도시기금\": 20789}, {\"국민은행\": 5811}, {\"우리은행\": 4134}, {\"신한은행\": 1198}, {\"한국시티은행\": 288}, {\"하나은행\": 3443}, {\"농협은행/수협은행\": 2299}, {\"외환은행\": 2187}, {\"기타은행\": 1061}]}, {\"year\": 2007, \"total_amount\": 50893, \"detail_amount\": [{\"주택도시기금\": 27745}, {\"국민은행\": 8260}, {\"우리은행\": 3545}, {\"신한은행\": 2497}, {\"한국시티은행\": 139}, {\"하나은행\": 2279}, {\"농협은행/수협은행\": 3515}, {\"외환은행\": 2059}, {\"기타은행\": 854}]}, {\"year\": 2008, \"total_amount\": 67603, \"detail_amount\": [{\"주택도시기금\": 35721}, {\"국민은행\": 12786}, {\"우리은행\": 4290}, {\"신한은행\": 1701}, {\"한국시티은행\": 69}, {\"하나은행\": 1706}, {\"농협은행/수협은행\": 9630}, {\"외환은행\": 941}, {\"기타은행\": 759}]}, {\"year\": 2009, \"total_amount\": 96545, \"detail_amount\": [{\"주택도시기금\": 44735}, {\"국민은행\": 8682}, {\"우리은행\": 13105}, {\"신한은행\": 3023}, {\"한국시티은행\": 40}, {\"하나은행\": 1226}, {\"농협은행/수협은행\": 8775}, {\"외환은행\": 6908}, {\"기타은행\": 10051}]}, {\"year\": 2010, \"total_amount\": 114903, \"detail_amount\": [{\"주택도시기금\": 50554}, {\"국민은행\": 16017}, {\"우리은행\": 15846}, {\"신한은행\": 2724}, {\"한국시티은행\": 22}, {\"하나은행\": 1872}, {\"농협은행/수협은행\": 10984}, {\"외환은행\": 11158}, {\"기타은행\": 5726}]}, {\"year\": 2011, \"total_amount\": 206693, \"detail_amount\": [{\"주택도시기금\": 69236}, {\"국민은행\": 29118}, {\"우리은행\": 29572}, {\"신한은행\": 11106}, {\"한국시티은행\": 13}, {\"하나은행\": 9283}, {\"농협은행/수협은행\": 19847}, {\"외환은행\": 8192}, {\"기타은행\": 30326}]}, {\"year\": 2012, \"total_amount\": 275591, \"detail_amount\": [{\"주택도시기금\": 84227}, {\"국민은행\": 37597}, {\"우리은행\": 38278}, {\"신한은행\": 21742}, {\"한국시티은행\": 4}, {\"하나은행\": 12534}, {\"농협은행/수협은행\": 27253}, {\"외환은행\": 19975}, {\"기타은행\": 33981}]}, {\"year\": 2013, \"total_amount\": 265805, \"detail_amount\": [{\"주택도시기금\": 89823}, {\"국민은행\": 33063}, {\"우리은행\": 37661}, {\"신한은행\": 21330}, {\"한국시티은행\": 50}, {\"하나은행\": 15167}, {\"농협은행/수협은행\": 17908}, {\"외환은행\": 10619}, {\"기타은행\": 40184}]}, {\"year\": 2014, \"total_amount\": 318771, \"detail_amount\": [{\"주택도시기금\": 96184}, {\"국민은행\": 48338}, {\"우리은행\": 52085}, {\"신한은행\": 28526}, {\"한국시티은행\": 183}, {\"하나은행\": 20714}, {\"농협은행/수협은행\": 20861}, {\"외환은행\": 11183}, {\"기타은행\": 40697}]}, {\"year\": 2015, \"total_amount\": 374773, \"detail_amount\": [{\"주택도시기금\": 82478}, {\"국민은행\": 57749}, {\"우리은행\": 67999}, {\"신한은행\": 39239}, {\"한국시티은행\": 37}, {\"하나은행\": 37263}, {\"농협은행/수협은행\": 18541}, {\"외환은행\": 20421}, {\"기타은행\": 51046}]}, {\"year\": 2016, \"total_amount\": 400971, \"detail_amount\": [{\"주택도시기금\": 91017}, {\"국민은행\": 61380}, {\"우리은행\": 45461}, {\"신한은행\": 36767}, {\"한국시티은행\": 46}, {\"하나은행\": 45485}, {\"농협은행/수협은행\": 23913}, {\"외환은행\": 5977}, {\"기타은행\": 90925}]}, {\"year\": 2017, \"total_amount\": 295126, \"detail_amount\": [{\"주택도시기금\": 85409}, {\"국민은행\": 31480}, {\"우리은행\": 38846}, {\"신한은행\": 40729}, {\"한국시티은행\": 7}, {\"하나은행\": 35629}, {\"농협은행/수협은행\": 26969}, {\"외환은행\": 0}, {\"기타은행\": 36057}]}]}");	
	}
	
	@Test
	@Order(7)
	public void minMaxAmountTest() throws Exception {
		String result = fc.getMinMaxAmount();

		assertEquals(result, "{\"name\": \"외환은행\", \"support_amount\": [{\"2017\": 0.0000}, {\"2015\": 1701.7500}]}");
	}
	
	@Test
	@Order(8)
	public void predictTest() throws Exception {
		Finance finance = new Finance();
		
		finance.setBank("국민은행");
		finance.setMonth(2);
		
		String result = fc.getPredict(finance);

		assertEquals(result, "{\"bank\":\"KB\",\"amount\":4818,\"month\":2,\"year\":2018}");
	}
}
