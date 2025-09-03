package com.opendata.domain.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opendata.global.jwt.JwtUtil;
import com.opendata.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthTestController {
	private final JwtUtil jwtUtil;

	@PostMapping
	public ResponseEntity<ApiResponse<Map<String, String>>> createDevToken(@RequestParam String email) {
		String accessToken = jwtUtil.createAccess(email);
		String refreshToken = jwtUtil.createRefresh(email);

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access", accessToken);
		tokens.put("refresh", refreshToken);

		return ResponseEntity.ok(ApiResponse.onSuccess(tokens));
	}
}
