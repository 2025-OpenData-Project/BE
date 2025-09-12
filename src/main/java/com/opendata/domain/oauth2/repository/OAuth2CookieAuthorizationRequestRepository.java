package com.opendata.domain.oauth2.repository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import java.util.Base64;

@Component
@Slf4j
public class OAuth2CookieAuthorizationRequestRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final String AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	private static final String REDIRECT_URI_COOKIE_NAME = "redirect_uri";
	private static final String MODE_COOKIE_NAME = "mode";
	private static final int COOKIE_EXPIRATION = 180;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return getCookieValue(request, AUTH_REQUEST_COOKIE_NAME)
			.map(this::deserialize)
			.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request,
		HttpServletResponse response) {

		if (authorizationRequest == null) {
			deleteCookie(response, AUTH_REQUEST_COOKIE_NAME);
			deleteCookie(response, REDIRECT_URI_COOKIE_NAME);
			deleteCookie(response, MODE_COOKIE_NAME);
			return;
		}

		addCookie(response, AUTH_REQUEST_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRATION);

		String redirectUri = request.getParameter("redirect_uri");
		if (StringUtils.hasText(redirectUri)) {
			addCookie(response, REDIRECT_URI_COOKIE_NAME, redirectUri, COOKIE_EXPIRATION);
		}

		String mode = request.getParameter("mode");
		if (StringUtils.hasText(mode)) {
			addCookie(response, MODE_COOKIE_NAME, mode, COOKIE_EXPIRATION);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return loadAuthorizationRequest(request);
	}

	public void removeCookies(HttpServletResponse response) {
		deleteCookie(response, AUTH_REQUEST_COOKIE_NAME);
		deleteCookie(response, REDIRECT_URI_COOKIE_NAME);
		deleteCookie(response, MODE_COOKIE_NAME);
	}

	public String loadRedirectUri(HttpServletRequest request) {

		return getCookieValue(request, REDIRECT_URI_COOKIE_NAME).orElse("http://localhost:3000/");
	}

	public String loadMode(HttpServletRequest request) {
		return getCookieValue(request, MODE_COOKIE_NAME).orElse(null);
	}

	private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	private void deleteCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	private java.util.Optional<String> getCookieValue(HttpServletRequest request, String name) {
		if (request.getCookies() == null)
			return java.util.Optional.empty();
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(name)) {
				log.debug("Found cookie: {}", cookie.getValue());
				return java.util.Optional.of(cookie.getValue());
			}
		}
		return java.util.Optional.empty();
	}

	private String serialize(OAuth2AuthorizationRequest authRequest) {
		byte[] bytes = SerializationUtils.serialize(authRequest);
		return Base64.getUrlEncoder().encodeToString(bytes);
	}

	private OAuth2AuthorizationRequest deserialize(String value) {
		byte[] bytes = Base64.getUrlDecoder().decode(value);
		return (OAuth2AuthorizationRequest)SerializationUtils.deserialize(bytes);
	}
}
