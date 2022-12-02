package com.kyobo.platform.recipe.controller;

import java.sql.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
//import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;
import com.kyobo.platform.recipe.service.RecipeMainService;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipeMain")
@RestController
@RequiredArgsConstructor
public class RecipeMainController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeMainController.class);
	
//	private final RedisService redisService = new RedisService();
	
	private final RecipeMainService recipeMainService;
	
//	public String recipeRegist(@RequestBody RecipeRegist recipe) {
		// Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
//  }
	
	// 메인화면 정보
	@RequestMapping(value = "/mainInfo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String mainInfo() {
		logger.info("====================== mainInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			LinkedHashMap<String, Object> main_info = recipeMainService.mainInfo();
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("main_info", main_info);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== mainInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== mainInfo controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 맞춤기반 메인 레시피 목록
	@RequestMapping(value = "/recipeCustomBasedList/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String recipeCustomBasedList(@PathVariable("user_id") String user_id) {
		logger.info("====================== recipeCustomBasedList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_custom_based_list = recipeMainService.recipeCustomBasedList(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_custom_based_list", recipe_custom_based_list);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeCustomBasedList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeCustomBasedList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 사용자기반 메인 레시피 목록
	@RequestMapping(value = "/recipeUserBasedList/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String recipeUserBasedList(@PathVariable("user_id") String user_id) {
		logger.info("====================== recipeUserBasedList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_user_based_list = recipeMainService.recipeUserBasedList(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_user_based_list", recipe_user_based_list);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeUserBasedList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeUserBasedList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 비회원기반 메인 레시피 목록
	@RequestMapping(value = "/noUserCustomBasedRecipeList/{age}/{sex}/{height}/{weight}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String noUserCustomBasedRecipeList(@PathVariable("age") String age, @PathVariable("sex") String sex,
			@PathVariable("height") String height, @PathVariable("weight") String weight) {
		logger.info("====================== noUserCustomBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_custom_based_list = 
					recipeMainService.noUserCustomBasedRecipeList(age, sex, height, weight);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_no_user_custom_based_list", recipe_no_user_custom_based_list);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserCustomBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserCustomBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 비회원 알레르기기반 메인 레시피 목록
	@RequestMapping(value = "/noUserAllergyBasedRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String noUserAllergyBasedRecipeList(@RequestBody Array allergy) {
		logger.info("====================== noUserAllergyBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_allergy_based_list = recipeMainService.noUserAllergyBasedRecipeList(allergy);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_no_user_allergy_based_list", recipe_no_user_allergy_based_list);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserAllergyBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserAllergyBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 비회원 건강기반 메인 레시피 목록
	@RequestMapping(value = "/noUserHealthBasedRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String noUserHealthBasedRecipeList() {
		logger.info("====================== noUserHealthBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_health_based_list = recipeMainService.noUserHealthBasedRecipeList();
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_no_user_health_based_list", recipe_no_user_health_based_list);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserHealthBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== noUserHealthBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 제철재료기반 메인 레시피 목록
	@RequestMapping(value = "/seasonIngredientBasedRecipeList/{season_ingredient}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String seasonIngredientBasedRecipeList(@PathVariable("season_ingredient") String season_ingredient) {
		logger.info("====================== seasonIngredientBasedRecipeList controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_season_ingredient_based_list = recipeMainService.seasonIngredientBasedRecipeList(season_ingredient);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_season_ingredient_based_list", recipe_season_ingredient_based_list);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== seasonIngredientBasedRecipeList controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== seasonIngredientBasedRecipeList controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 아이정보
	@RequestMapping(value = "/userBabyInfo/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String userBabyInfo(@PathVariable("user_id") String user_id) {
		logger.info("====================== userBabyInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			LinkedHashMap<String, Object> baby_info = recipeMainService.userBabyInfo(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("baby_info", baby_info);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== userBabyInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== userBabyInfo controller error ======================");
	        return json_recipe_list;
		}
	}
}
