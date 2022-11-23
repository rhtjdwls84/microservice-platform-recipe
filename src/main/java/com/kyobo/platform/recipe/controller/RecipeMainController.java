package com.kyobo.platform.recipe.controller;

import java.sql.Array;
import java.util.HashMap;
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
	
	// 맞춤기반 메인 레시피 목록
	@RequestMapping(value = "/recipeCustomBasedList/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String recipeCustomBasedList(@PathVariable("user_id") String user_id) {
		logger.info("====================== recipeCustomBasedList controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_custom_based_list = recipeMainService.recipeCustomBasedList(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_custom_based_list", recipe_custom_based_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeCustomBasedList controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeCustomBasedList controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 사용자기반 메인 레시피 목록
	@RequestMapping(value = "/recipeUserBasedList/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String recipeUserBasedList(@PathVariable("user_id") String user_id) {
		logger.info("====================== recipeUserBasedList controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_user_based_list = recipeMainService.recipeUserBasedList(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_user_based_list", recipe_user_based_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeUserBasedList controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeUserBasedList controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 비회원기반 메인 레시피 목록
	@RequestMapping(value = "/noUserCustomBasedRecipeList/{age}/{sex}/{height}/{weight}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String noUserCustomBasedRecipeList(@PathVariable("age") String age, @PathVariable("sex") String sex,
			@PathVariable("height") String height, @PathVariable("weight") String weight) {
		logger.info("====================== noUserCustomBasedRecipeList controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_custom_based_list = 
					recipeMainService.noUserCustomBasedRecipeList(age, sex, height, weight);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_no_user_custom_based_list", recipe_no_user_custom_based_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== noUserCustomBasedRecipeList controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== noUserCustomBasedRecipeList controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 비회원 알레르기기반 메인 레시피 목록
	@RequestMapping(value = "/noUserAllergyBasedRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String noUserAllergyBasedRecipeList(@RequestBody Array allergy) {
		logger.info("====================== noUserAllergyBasedRecipeList controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_allergy_based_list = recipeMainService.noUserAllergyBasedRecipeList(allergy);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_no_user_allergy_based_list", recipe_no_user_allergy_based_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== noUserAllergyBasedRecipeList controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== noUserAllergyBasedRecipeList controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 비회원 건강기반 메인 레시피 목록
	@RequestMapping(value = "/noUserHealthBasedRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String noUserHealthBasedRecipeList(@RequestBody Array health_keywords) {
		logger.info("====================== noUserHealthBasedRecipeList controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_no_user_health_based_list = recipeMainService.noUserHealthBasedRecipeList(health_keywords);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_no_user_health_based_list", recipe_no_user_health_based_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== noUserHealthBasedRecipeList controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== noUserHealthBasedRecipeList controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 제철재료기반 메인 레시피 목록
	@RequestMapping(value = "/seasonMaterialBasedRecipeList/{season_material}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String seasonMaterialBasedRecipeList(@PathVariable("season_material") String season_material) {
		logger.info("====================== seasonMaterialBasedRecipeList controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_season_material_based_list = recipeMainService.seasonMaterialBasedRecipeList(season_material);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_season_material_based_list", recipe_season_material_based_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== seasonMaterialBasedRecipeList controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== seasonMaterialBasedRecipeList controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 아이정보
	@RequestMapping(value = "/userBabyInfo/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String userBabyInfo(@PathVariable("user_id") String user_id) {
		logger.info("====================== userBabyInfo controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			HashMap<String, Object> baby_info = recipeMainService.userBabyInfo(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("baby_info", baby_info);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== userBabyInfo controller end ======================");
	        return jsonRecipeList;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== userBabyInfo controller error ======================");
	        return jsonRecipeList;
		}
	}
}
