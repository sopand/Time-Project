package com.time.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="관리자 API",description = "관리자")
@RequestMapping("/api/admin")
public class AdminController {

	
	@GetMapping("/")
	public String adminCheck() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(name);
		return "Admin Controller";
	}
	
}
