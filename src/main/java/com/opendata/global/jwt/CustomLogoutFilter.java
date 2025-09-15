package com.opendata.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

	private final JwtUtil jwtUtil;

	@Override
	public void doFilter(
		jakarta.servlet.ServletRequest servletRequest,
		jakarta.servlet.ServletResponse servletResponse,
		FilterChain filterChain
	) throws IOException, ServletException {
		doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {

		String requestUri = request.getRequestURI();
		if (!requestUri.equals("/logout") && !requestUri.equals("/api/logout")) {
			chain.doFilter(request, response);
			return;
		}

		String requestMethod = request.getMethod();
		if (!requestMethod.equals("POST")) {
			chain.doFilter(request, response);
			return;
		}

		String refresh = CookieUtil.getRefreshTokenFromRequest(request).orElse(null);

		if (refresh == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			jwtUtil.isExpired(refresh);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String category = jwtUtil.getCategory(refresh);
		if (!"refresh".equals(category)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String domain = ".yourse-seoul.com";
		response.addHeader("Set-Cookie",
			"refresh=; Max-Age=0; Path=/; Domain=" + domain + "; HttpOnly; Secure; SameSite=Strict");
		response.addHeader("Set-Cookie",
			"access=; Max-Age=0; Path=/; Domain=" + domain + "; HttpOnly; Secure; SameSite=Strict");

		response.setStatus(HttpServletResponse.SC_OK);
	}
}
