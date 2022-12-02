package com.kyobo.platform.recipe.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.dao.RecipeIngredient;
import com.kyobo.platform.recipe.dao.RecipeOrder;
//import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;
import com.kyobo.platform.recipe.service.RecipeRegistService;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipe")
@RestController
@RequiredArgsConstructor
public class RecipeRegistController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistController.class);
	
//	private final RedisService redisService = new RedisService();
	
	private final RecipeRegistService recipeRegistService;
	
//	public String recipeRegist(@RequestBody RecipeRegist recipe) {
		// Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
//    }
	
	// 레시피 기본정보 작성
	@RequestMapping(value = "/recipeDefInfo", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String recipeDefInfo(@RequestBody Recipe recipe) {
		logger.info("====================== recipeDefInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			String recipe_key = recipeRegistService.recipeDefInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeDefInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeDefInfo controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 레시피 부가정보 작성
	@RequestMapping(value = "/recipeAddInfo/{recipe_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.PATCH)
	@ResponseBody
	public String recipeAddInfo(@RequestBody Recipe recipe, @PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeAddInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipe_key = recipeRegistService.recipeAddInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeAddInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeAddInfo controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 레시피 재료정보 작성
	@RequestMapping(value = "/recipeIngredientInfo/{recipe_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.PATCH)
	@ResponseBody
	public String recipeIngredientInfo(@RequestParam("recipe_servings") String recipe_servings, 
			@RequestParam("recipe_ingredient_list") String recipe_ingredient_list, @PathVariable("recipe_key") String recipe_key) 
					throws JsonMappingException, JsonProcessingException {
		logger.info("====================== recipeIngredientInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		
		Recipe recipe = new Recipe();
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
		ArrayList<RecipeIngredient> recipe_ingredient_arr_list = objectMapper.readValue(recipe_ingredient_list, new TypeReference<>(){});
		
		recipe.setRecipe_ingredient_list(recipe_ingredient_arr_list);
		recipe.setRecipe_servings(Integer.parseInt(recipe_servings));
		recipe.setRecipe_key(recipe_key);
		
		Gson gson = new Gson();
		
		try {
			recipe_key = recipeRegistService.recipeIngredientInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeIngredientInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeIngredientInfo controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 레시피 순서정보 작성
	@RequestMapping(value = "/recipeOrderInfo/{recipe_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.PATCH)
	@ResponseBody
	public String recipeOrderInfo(@RequestParam("recipe_order_list") String recipe_order_list, 
			@PathVariable("recipe_key") String recipe_key) throws JsonMappingException, JsonProcessingException {
		logger.info("====================== recipeOrderInfo controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		Recipe recipe = new Recipe();
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
		ArrayList<RecipeOrder> recipe_order_arr_list = objectMapper.readValue(recipe_order_list, new TypeReference<>(){});
		
		recipe.setRecipe_order_list(recipe_order_arr_list);
		recipe.setRecipe_key(recipe_key);
		
		try {
			recipe_key = recipeRegistService.recipeOrderInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeOrderInfo controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeOrderInfo controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 이미지 업로드
	@Async
	@RequestMapping(value = "/recipeImageUpload/{recipe_key}/{recipe_image_type}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.PUT)
	@ResponseBody
    public void recipeImageUpload(@RequestBody List<MultipartFile> recipe_image_list, @PathVariable("recipe_key") String recipe_key
    		, @PathVariable("recipe_image_type") String recipe_image_type) {
		logger.info("====================== recipeImageUpload controller start ======================");
		
		try {
			recipeRegistService.recipeImageUpload(recipe_image_list, recipe_key, recipe_image_type);
			
			logger.info("====================== recipeImageUpload controller end ======================");
		} catch(Exception e) {
			e.printStackTrace();
			
			logger.info("====================== recipeImageUpload controller error ======================");
		}
    }
	
	// 레시피 임시저장 체크
	@RequestMapping(value = "/checkRecipeTempSave/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String checkRecipeTempSave(@PathVariable("user_id") String user_id) {
		logger.info("====================== checkRecipeTempSave controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			String recipe_key = recipeRegistService.CheckRecipeTempSave(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== checkRecipeTempSave controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== checkRecipeTempSave controller error ======================");
	        return json_recipe_list;
		}
	}
	
	// 레시피 임시저장 삭제(새로작성시)
	@RequestMapping(value = "/deleteRecipeTempSave/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteRecipeTempSave(@PathVariable("user_id") String user_id) {
		logger.info("====================== deleteRecipeTempSave controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipeRegistService.deleteRecipeTempSave(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== deleteRecipeTempSave controller end ======================");
			return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== deleteRecipeTempSave controller error ======================");
			return json_recipe_list;
		}
	}
		
	// 레시피 베이스 재료 조회
	@RequestMapping(value = "/listRecipeBaseIngredient/{recipe_ingredient_category}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String listRecipeBaseIngredient(@PathVariable("recipe_ingredient_category") String search_text) {
		logger.info("====================== listRecipeBaseIngredient controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			JSONObject recipe_base_ingredient_json = recipeRegistService.listRecipeBaseIngredient(search_text);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_base_ingredient_json", recipe_base_ingredient_json);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== listRecipeBaseIngredient controller end ======================");
			return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== listRecipeBaseIngredient controller error ======================");
			return json_recipe_list;
		}
	}
	
	// 레시피 재료 검색
	@RequestMapping(value = "/listRecipeIngredient/{text}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String listRecipeIngredient(@PathVariable("text") String text) {
		logger.info("====================== listRecipeIngredient controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			JSONObject recipe_ingredient_json = recipeRegistService.listRecipeIngredient(text);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_ingredient_json", recipe_ingredient_json);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== listRecipeIngredient controller end ======================");
			return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== listRecipeIngredient controller error ======================");
			return json_recipe_list;
		}
	}
	
	// 레시피 업로드
	@RequestMapping(value = "/recipeUpload/{recipe_key}", produces = "application/json; charset=UTF-8", method = RequestMethod.PATCH)
	@ResponseBody
	public String recipeUpload(@PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeUpload controller start ======================");
		
		String json_recipe_list = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipeRegistService.recipeUpload(recipe_key);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeUpload controller end ======================");
	        return json_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			json_recipe_list = gson.toJson(map);
			
			logger.info("====================== recipeUpload controller error ======================");
	        return json_recipe_list;
		}
	}
}
