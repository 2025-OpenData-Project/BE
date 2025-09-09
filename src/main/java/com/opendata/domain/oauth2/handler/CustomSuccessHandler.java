package com.opendata.domain.oauth2.handler;

import com.opendata.domain.oauth2.dto.user.CustomOAuth2User;
import com.opendata.domain.oauth2.repository.OAuth2CookieAuthorizationRequestRepository;
import com.opendata.global.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtUtil jwtUtil;
    private final OAuth2CookieAuthorizationRequestRepository authRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String email = customUserDetails.getEmail();
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauth2Token.getAuthorizedClientRegistrationId(),
                oauth2Token.getName()
        );

        String accessToken = authorizedClient.getAccessToken().getTokenValue();


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createAccess(email);
        String refresh = jwtUtil.createRefresh(email);

        response.addCookie(createCookie("access", token,request));
        response.addCookie(createCookie("refresh", refresh,request));
        //response.addCookie(createCookie("google_access_token", accessToken));
        String targetUrl = authRequestRepository.loadRedirectUri(request);
        authRequestRepository.removeCookies(response);

        response.sendRedirect(targetUrl);
    }

    private Cookie createCookie(String key, String value, HttpServletRequest request) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60); // 1시간
        cookie.setPath("/");

        String host = request.getServerName();
        log.info("test{}",host);
        if (host.contains("localhost")) {
            // 🔎 개발 모드
            cookie.setDomain("localhost");
            cookie.setHttpOnly(false); // document.cookie로 볼 수 있음
            cookie.setSecure(false);   // http:// 에서도 동작
        } else {
            // 🔒 운영 모드
            cookie.setDomain(".yourse-seoul.com");
            cookie.setHttpOnly(true);  // JS에서 못 읽음 (보안)
            cookie.setSecure(true);    // https:// 에서만
        }

        return cookie;
    }

}
