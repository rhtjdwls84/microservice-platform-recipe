package com.kyobo.platform.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@RequestMapping("/kyobo")
	@ResponseBody
    public String index() {
        return "안녕하세요. 게시판에 오신걸 환영합니다.";     
    }
	
	@RequestMapping("/kyobo3")
	public String root() {
		return "redirect:/question/list";
	}
}
