//package com.opendata.domain.oauth2.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class OAuth2RedirectUriCookieFilter extends OncePerRequestFilter {
//    private static final String REDIRECT_URI_PARAM = "redirect_uri";
//    private static final String REDIRECT_URI_COOKIE = "redirect_uri";
//    private static final int COOKIE_EXPIRE_SECONDS = 180;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String redirectUri = request.getParameter(REDIRECT_URI_PARAM);
//
//        if (StringUtils.hasText(redirectUri)) {
//            Cookie cookie = new Cookie(REDIRECT_URI_COOKIE, redirectUri);
//            cookie.setPath("/");
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(COOKIE_EXPIRE_SECONDS);
//            response.addCookie(cookie);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return !request.getRequestURI().startsWith("/oauth2/authorization");
//    }
//}
