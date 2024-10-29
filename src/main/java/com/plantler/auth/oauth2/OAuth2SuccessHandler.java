package com.plantler.auth.oauth2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.plantler.auth.JwtToken;
import com.plantler.dto.RequestTokenDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final JwtToken jwtToken;
	
	@Value("${FRONT.END.URI}")
	private String HOST;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(getUrl((CustomOAuth2User) authentication.getPrincipal()));
	}
	
	private String getUrl(CustomOAuth2User oAuth2User) {
		String issuer = oAuth2User.getIssuer();
        String id = oAuth2User.getId();
        String name = "";
		try {
			name = URLEncoder.encode(oAuth2User.getName(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        String email = oAuth2User.getEmail();
        int statusCode = oAuth2User.getStatusCode();
        log.info("{} >> id : {}, name : {}, email : {}, statusCode : {}", issuer, id, name, email, statusCode);
        String accessToken = (statusCode > 0) ? jwtToken.setToken(RequestTokenDTO.builder().type(issuer).id(id).build()) : null;
        return (accessToken == null) ? HOST.concat("/login") : 
        	HOST.concat("/auth/").concat(accessToken).concat("/").concat(issuer).concat("/").concat(name);
	}
	
}
