//package com.opendata.domain.register.controller;
//
//import com.opendata.domain.register.dto.RegisterRequest;
//import com.opendata.domain.register.service.RegisterService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/register")
//public class RegisterController {
//    private final RegisterService registerService;
//
//    @PostMapping("/new")
//    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){
//        return ResponseEntity.ok(registerService.registerUser(registerRequest));
//    }
//
//    //true -> 사용 가능, false -> 사용 불가.
//    @GetMapping("/avail-check")
//    public ResponseEntity<Boolean> checkEmailAvailable(@RequestParam String email){
//        return ResponseEntity.ok(registerService.checkEmail(email));
//    }
//
//
//}
