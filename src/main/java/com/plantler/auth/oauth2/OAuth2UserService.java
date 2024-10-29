package com.plantler.auth.oauth2;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantler.dto.UserDTO;
import com.plantler.mapper.LoginMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
	
	private final LoginMapper loginMapper;

	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String oAuthClientName = request.getClientRegistration().getClientName();

        try {
            log.info("{} : {}", oAuthClientName, new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String id = null;
        String name = null;
        String email = null;
        int statusCode = 2;

        if(oAuthClientName.equals("naver")) {
            Map<String, String> responseMap = (Map) oAuth2User.getAttributes().get("response");
            id = responseMap.get("id");
            email = responseMap.get("email");
            name = responseMap.get("nickname");
        }

        if(oAuthClientName.equals("kakao")) {
        	id = oAuth2User.getAttributes().get("id").toString();
        	Map<String, Object> kakao_account = (Map) oAuth2User.getAttributes().get("kakao_account");
        	if(kakao_account != null) {
        		log.info("kakao_account : {}", kakao_account);
        		Object oEmail = kakao_account.get("email");
        		if(oEmail != null) {
        			email = oEmail.toString();
        		}
        		log.info("email : {}", email);
        		Map<String, Object> profile = (Map) kakao_account.get("profile");
        		if(profile != null) {
        			log.info("profile : {}", profile);
        			name = profile.get("nickname").toString();
        		}
        	}
        }
        
        // 신규 회원이면 데이터베이스 테이블에 추가하는 로직
        if(loginMapper.findById(id) == null) {
            UserDTO user = UserDTO.builder()
	                .user_id(id)
	                .user_email(email)
	                .user_nick(name)
	                .user_provider(oAuthClientName)
	                .build();
            statusCode = loginMapper.save(user);
        }
        
        return CustomOAuth2User.builder()
        		.issuer(oAuthClientName)
        		.id(id)
        		.name(name)
        		.email(email)
        		.statusCode(statusCode)
        		.build();
    }
	
}
