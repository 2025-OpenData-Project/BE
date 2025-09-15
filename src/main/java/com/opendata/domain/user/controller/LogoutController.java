package com.opendata.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opendata.docs.LogoutControllerDocs;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LogoutController implements LogoutControllerDocs {

	@PostMapping("/logout")
	public ResponseEntity<String> doLogout(){
		return ResponseEntity.ok().build();
	}
}
