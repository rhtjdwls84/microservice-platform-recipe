package com.kyobo.platform.recipe.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.service.RecipeSearchService;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipeSearch")
@RestController
@RequiredArgsConstructor
public class RecipeSearchController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeSearchController.class);
	
	private final RecipeSearchService recipeSearchService;
	
	// 레시피 검색 조회
	@RequestMapping(value = "/searchRecipe", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String searchRecipe(@RequestParam(required = false) String recipe_search_text,
			@RequestParam(required = false) String category_main_name, @RequestParam(required = false) String category_name,
			@RequestParam(required = false) String recipe_babyfood_step, @RequestParam(required = false) String except_ingredient_yn,
			@RequestParam(required = false) String recipe_tag_desc) {
		logger.info("====================== searchRecipe controller start ======================");
		
		String jsonRecipeList = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map.Entry<String, Object>> recipe_list = recipeSearchService.searchRecipe(
					recipe_search_text, category_main_name, category_name, recipe_babyfood_step, except_ingredient_yn, recipe_tag_desc);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_list", recipe_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchRecipe controller end ======================");
			return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchRecipe controller error ======================");
			return jsonRecipeList;
		}
	}
}
