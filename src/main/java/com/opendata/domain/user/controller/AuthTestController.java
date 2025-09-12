package com.opendata.domain.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opendata.domain.user.service.UserService;
import com.opendata.global.jwt.JwtUtil;
import com.opendata.global.response.ApiResponse;
import com.opendata.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthTestController {
	private final JwtUtil jwtUtil;
	private final UserService userService;

	@PostMapping
	public ResponseEntity<ApiResponse<Map<String, String>>> createDevToken(@RequestParam String email) {
		String accessToken = jwtUtil.createAccess(email);
		String refreshToken = jwtUtil.createRefresh(email);

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access", accessToken);
		tokens.put("refresh", refreshToken);

		return ResponseEntity.ok(ApiResponse.onSuccess(tokens));
	}

	@GetMapping("/test")
	public ResponseEntity<ApiResponse<Boolean>> test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		try {
			userService.findUserByEmailOrThrow(customUserDetails);
			return ResponseEntity.ok(ApiResponse.onSuccess(true));
		} catch (Exception e) {
			return ResponseEntity.ok(ApiResponse.onSuccess(false));
		}


	}
}
