package com.kyobo.platform.recipe.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.service.RecipeMainService;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipeMain")
@RestController
@RequiredArgsConstructor
public class RecipeMainController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeMainController.class);
	
	private final RecipeMainService recipeMainService;
	
	// 메인화면 정보
	@RequestMapping(value = "/mainInfo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String mainInfo() {
		logger.info("====================== mainInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			LinkedHashMap<String, Object> main_info = recipeMainService.mainInfo();
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("main_info", main_info);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== mainInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== mainInfo controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 맞춤기반 메인 레시피 목록
	@RequestMapping(value = "/recipeCustomBasedList/{user_key}/{more_yn}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String recipeCustomBasedList(@PathVariable("user_key") String user_key, @PathVariable("more_yn") String more_yn) {
		logger.info("====================== recipeCustomBasedList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_custom_based_list = recipeMainService.recipeCustomBasedList(user_key, more_yn);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_custom_based_list", recipe_custom_based_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeCustomBasedList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeCustomBasedList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 사용자기반 메인 레시피 목록
	@RequestMapping(value = "/recipeUserBodyBasedList/{user_key}/{more_yn}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String recipeUserBodyBasedList(@PathVariable("user_key") String user_key, @PathVariable("more_yn") String more_yn) {
		logger.info("====================== recipeUserBodyBasedList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_user_based_list = recipeMainService.recipeUserBodyBasedList(user_key, more_yn);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_user_based_list", recipe_user_based_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeUserBodyBasedList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeUserBodyBasedList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 비회원 신체 기반 메인 레시피 목록
	@RequestMapping(value = "/noUserBodyBasedRecipeList/{gender}/{birthday}/{height}/{weight}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String noUserBodyBasedRecipeList(@PathVariable("gender") String gender, @PathVariable("birthday") String birthday,
			@PathVariable("height") String height, @PathVariable("weight") String weight) {
		logger.info("====================== noUserBodyBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_custom_based_list = 
					recipeMainService.noUserBodyBasedRecipeList(gender, birthday, height, weight);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_no_user_custom_based_list", recipe_no_user_custom_based_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserBodyBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserBodyBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 비회원 알레르기기반 메인 레시피 목록
	@RequestMapping(value = "/noUserAllergyBasedRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String noUserAllergyBasedRecipeList(@RequestBody ArrayList<Map<String, Object>> baby_allergy_list) {
		logger.info("====================== noUserAllergyBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_allergy_based_list = recipeMainService.noUserAllergyBasedRecipeList(baby_allergy_list);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_no_user_allergy_based_list", recipe_no_user_allergy_based_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserAllergyBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserAllergyBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 비회원 건강기반 메인 레시피 목록
	@RequestMapping(value = "/noUserHealthBasedRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String noUserHealthBasedRecipeList(ArrayList<Map<String, Object>> baby_concern_list) {
		logger.info("====================== noUserHealthBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_health_based_list = recipeMainService.noUserHealthBasedRecipeList(baby_concern_list);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_no_user_health_based_list", recipe_no_user_health_based_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserHealthBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserHealthBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 제철재료기반 메인 레시피 목록
	@RequestMapping(value = "/seasonIngredientBasedRecipeList/{season_ingredient}/{more_yn}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String seasonIngredientBasedRecipeList(@PathVariable("season_ingredient") String season_ingredient,
			@PathVariable("more_yn") String more_yn) {
		logger.info("====================== seasonIngredientBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_season_ingredient_based_list = 
					recipeMainService.seasonIngredientBasedRecipeList(season_ingredient, more_yn);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("recipe_season_ingredient_based_list", recipe_season_ingredient_based_list);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== seasonIngredientBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== seasonIngredientBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 아이정보
	@RequestMapping(value = "/userBabyInfo/{user_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String userBabyInfo(@PathVariable("user_key") String user_key) {
		logger.info("====================== userBabyInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> header_map = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> body_map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			LinkedHashMap<String, Object> baby_info = recipeMainService.userBabyInfo(user_key);
			
			header_map.put("response_code", "200");
			header_map.put("response_desc", "ok");
			body_map.put("baby_info", baby_info);
			
			map.put("dataheader", header_map);
			map.put("databody", body_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== userBabyInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			header_map.put("response_code", "500");
			header_map.put("response_desc", e);
			
			map.put("dataheader", header_map);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== userBabyInfo controller error ======================");
	        return json_recipe_list;
		}
	}
}
