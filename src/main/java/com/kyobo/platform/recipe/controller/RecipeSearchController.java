package com.kyobo.platform.recipe.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
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
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map.Entry<String, Object>> recipe_list = recipeSearchService.searchRecipe(
					recipe_search_text, category_main_name, category_name, recipe_babyfood_step, except_ingredient_yn, recipe_tag_desc);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_list", recipe_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchRecipe controller end ======================");
			return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchRecipe controller error ======================");
			return jsonRecipeList;
		}
	}
	
	// 유저 레시피 목록
	@RequestMapping(value = "/searchUserRecipe/{user_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String searchUserRecipe(@PathVariable("user_key") String user_key) {
		logger.info("====================== searchUserRecipe controller start ======================");
		
		String jsonRecipeList = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			Map<String, Object> user_recipe_obj = recipeSearchService.searchUserRecipe(user_key);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("user_recipe_obj", user_recipe_obj);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchUserRecipe controller end ======================");
			return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchUserRecipe controller error ======================");
			return jsonRecipeList;
		}
	}
	
	// 타인 레시피 목록
	@RequestMapping(value = "/searchStrangerRecipe/{user_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String searchStrangerRecipe(@PathVariable("user_key") String user_key) {
		logger.info("====================== searchStrangerRecipe controller start ======================");
		
		String jsonRecipeList = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			Map<String, Object> stranger_recipe_obj = recipeSearchService.searchStrangerRecipe(user_key);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("stranger_recipe_obj", stranger_recipe_obj);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchStrangerRecipe controller end ======================");
			return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== searchStrangerRecipe controller error ======================");
			return jsonRecipeList;
		}
	}
}
