package com.kakao;

import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.kakao.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WebFluxFilter implements WebFilter {
	@Autowired
	UserService us;

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
		String path = serverWebExchange.getRequest().getPath().value();
		
		if(!path.equals("/signup") && !path.equals("/signin"))
		{
			boolean isError = false;
			String errorMessage = null;
			
			if(serverWebExchange.getRequest().getHeaders().get("Authorization") != null && serverWebExchange.getRequest().getHeaders().get("Authorization").size() > 0) {
				String jwt = serverWebExchange.getRequest().getHeaders().get("Authorization").get(0);

				try {
					us.checkJwt(jwt.substring(7));
		        } catch (ExpiredJwtException exception) {
		        	isError = true;
					errorMessage = "토큰 만료";
		        } catch (JwtException exception) {
		            isError = true;
					errorMessage = "토큰 변조";
		        } catch (Exception exception) {
		        	isError = true;
					errorMessage = "토큰 오류";
		        }
			} else {
				isError = true;
				errorMessage = "토큰 미입력";
			}
			
			if(isError) {
				System.out.println(errorMessage);
				
				JSONObject object = new JSONObject();
				object.put("message", errorMessage);
				
				byte[] bytes = object.toJSONString().getBytes(StandardCharsets.UTF_8);
		        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
				serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
			}
		}

		serverWebExchange.getResponse().getHeaders().add("web-filter", "web-filter-test");
		return webFilterChain.filter(serverWebExchange);
	}
}