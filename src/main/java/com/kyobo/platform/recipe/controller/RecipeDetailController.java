package com.kyobo.platform.recipe.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jets3t.service.CloudFrontServiceException;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.dao.RecipeReview;
//import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;
import com.kyobo.platform.recipe.service.RecipeDetailService;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipeDetail")
@RestController
@RequiredArgsConstructor
public class RecipeDetailController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeDetailController.class);
	
//	private final RedisService redisService = new RedisService();
	
	private final RecipeDetailService recipeDetailService;
	
//	public String recipeRegist(@RequestBody RecipeRegist recipe) {
		// Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
//    }
	
	// 레시피 상세 정보
	@RequestMapping(value = "/recipeDetail/{recipe_key}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String recipeDetail(@PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeDetail controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			Recipe recipe = recipeDetailService.recipeDetail(recipe_key);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_detail_info", recipe);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeDetail controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeDetail controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	// 레시피 리뷰 등록
	@RequestMapping(value = "/registRecipeReview", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.POST)
	@ResponseBody
    public String registRecipeReview(@RequestBody Recipe recipe) {
		logger.info("====================== registRecipeReview controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			HashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailService.registRecipeReview(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_review_cnt_info", recipe_review_cnt_info);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== registRecipeReview controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== registRecipeReview controller error ======================");
	        return jsonRecipeList;
		}
    }
	
	// 레시피 리뷰 등록
	@RequestMapping(value = "/registRecipeScrap", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.POST)
	@ResponseBody
    public String registRecipeScrap(@RequestBody Recipe recipe) {
		logger.info("====================== registRecipeScrap controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipeDetailService.registRecipeScrap(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== registRecipeScrap controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== registRecipeScrap controller error ======================");
	        return jsonRecipeList;
		}
    }
}
