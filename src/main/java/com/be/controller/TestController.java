package com.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/test")
	public String test() 
	{
		return "working";
	}

	public String Welcome()
	{
		return "welcome";
	}

}
