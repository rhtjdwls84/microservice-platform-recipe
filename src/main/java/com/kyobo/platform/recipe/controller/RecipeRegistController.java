package com.kyobo.platform.recipe.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jets3t.service.CloudFrontServiceException;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.dao.Recipe;
//import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;
import com.kyobo.platform.recipe.service.RecipeRegistService;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipe")
@RestController
@RequiredArgsConstructor
public class RecipeRegistController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistController.class);
	
//	private final RedisService redisService = new RedisService();
	
	private final RecipeRegistService recipeRegistService;
	
//	public String recipeRegist(@RequestBody RecipeRegist recipe) {
		//Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
//    }
	
	//레시피 기본정보 작성
	@RequestMapping(value = "/recipeDefInfo", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.POST)
	@ResponseBody
	public String recipeDefInfo(@RequestParam("recipe_def_info") Recipe recipe) {
		logger.info("====================== recipeDefInfo controller start ======================");
		
//		recipe.setRecipe_user_id("rhtjdwls84");
//		recipe.setRecipe_name("연어빠바요뜨");
//		recipe.setRecipe_desc("아이들이 좋아하는 레시피");
//		recipe.setRecipe_category("밥/죽");
//		recipe.setRecipe_main_img_key_name("20221031-071634982.csv");
//		recipe.setRecipe_main_img_path("s3://kyobo-ml/ml-rds-recipe/recipe_db_ml/RECIPE_MATERIAL_TB/");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			String recipe_key = recipeRegistService.recipeDefInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeDefInfo controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeDefInfo controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	//레시피 부가정보 작성
	@RequestMapping(value = "/recipeAddInfo/{recipe_key}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.PUT)
	@ResponseBody
	public String recipeAddInfo(@RequestParam("recipe_add_info") Recipe recipe, 
			@PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeAddInfo controller start ======================");
		
//		recipe.setRecipe_health_note("건강노트입니다");
//		recipe.setRecipe_lead_time("1시간이내");
//		recipe.setRecipe_level("쉬워요");
//		recipe.setRecipe_babyfood_step("중기 이유식");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipe_key = recipeRegistService.recipeAddInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeAddInfo controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeAddInfo controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	//레시피 재료정보 작성
	@RequestMapping(value = "/recipeMaterialInfo/{recipe_key}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.PUT)
	@ResponseBody
	public String recipeMaterialInfo(@RequestParam("recipe_material_info") Recipe recipe, 
			@PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeMaterialInfo controller start ======================");
		
//		recipe.setRecipe_servings(1);
//		
//		ArrayList<RecipeMaterial> recipeMaterialList = new ArrayList<RecipeMaterial>();
//
//		RecipeMaterial recipeMaterial = new RecipeMaterial();
//		recipeMaterial.setRecipe_material_name("연어");
//		recipeMaterial.setRecipe_material_classify("기본재료");
//		recipeMaterial.setRecipe_material_main_classify("해산물");
//		recipeMaterial.setRecipe_material_meature("1마리");
//		recipeMaterialList.add(recipeMaterial);
//		
//		RecipeMaterial recipeMaterial1 = new RecipeMaterial();
//		recipeMaterial1.setRecipe_material_name("양파");
//		recipeMaterial1.setRecipe_material_classify("기본재료");
//		recipeMaterial1.setRecipe_material_main_classify("채소류");
//		recipeMaterial1.setRecipe_material_meature("1개");
//		recipeMaterialList.add(recipeMaterial1);
//		
//		recipe.setRecipe_material_arr(recipeMaterialList);
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipe_key = recipeRegistService.recipeMaterialInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeMaterialInfo controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeMaterialInfo controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	//레시피 순서정보 작성
	@RequestMapping(value = "/recipeOrderInfo/{recipe_key}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.PUT)
	@ResponseBody
	public String recipeOrderInfo(@RequestParam("recipe_order_info") Recipe recipe, 
			@PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeOrderInfo controller start ======================");
		
//		ArrayList<RecipeOrder> recipeOrderList = new ArrayList<RecipeOrder>();
//
//		RecipeOrder recipeOrder = new RecipeOrder();
//		recipeOrder.setRecipe_order("1");
//		recipeOrder.setRecipe_order_desc("연어를 자른다");
//		recipeOrder.setRecipe_order_img_key_name("20221031-071634982.csv");
//		recipeOrder.setRecipe_order_img_path("s3://kyobo-ml/ml-rds-recipe/recipe_db_ml/RECIPE_MATERIAL_TB/");
//		recipeOrderList.add(0, recipeOrder);
//		
//		RecipeOrder recipeOrder1 = new RecipeOrder();
//		recipeOrder1.setRecipe_order("2");
//		recipeOrder1.setRecipe_order_desc("양파를 덮는다");
//		recipeOrder1.setRecipe_order_img_key_name("20221031-071634982.csv");
//		recipeOrder1.setRecipe_order_img_path("s3://kyobo-ml/ml-rds-recipe/recipe_db_ml/RECIPE_MATERIAL_TB/");
//		recipeOrderList.add(recipeOrder1);
//		
//		recipe.setRecipe_order_arr(recipeOrderList);
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipe_key = recipeRegistService.recipeOrderInfo(recipe);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeOrderInfo controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeOrderInfo controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	//이미지 업로드
	@RequestMapping(value = "/recipeImageUpload", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.POST)
	@ResponseBody
    public String recipeImageUpload(@RequestParam("recipe_image_list") List<MultipartFile> multipartFiles) 
    		throws IOException, ParseException, CloudFrontServiceException {
		logger.info("====================== recipeImageUpload controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map<String, Object>> recipe_image_url_list = recipeRegistService.recipeImageUpload(multipartFiles);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_image_url_list", recipe_image_url_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeImageUpload controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeImageUpload controller error ======================");
	        return jsonRecipeList;
		}
    }
	
	//레시피 임시저장 체크
	@RequestMapping(value = "/checkRecipeTempSave/{user_id}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String checkRecipeTempSave(@PathVariable("user_id") String user_id) {
		logger.info("====================== checkRecipeTempSave controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			String recipe_key = recipeRegistService.CheckRecipeTempSave(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_key", recipe_key);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== checkRecipeTempSave controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== checkRecipeTempSave controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	//레시피 임시저장 삭제(새로작성시)
	@RequestMapping(value = "/deleteRecipeTempSave/{user_id}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteRecipeTempSave(@PathVariable("user_id") String user_id) {
		logger.info("====================== deleteRecipeTempSave controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipeRegistService.deleteRecipeTempSave(user_id);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== deleteRecipeTempSave controller end ======================");
			return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== deleteRecipeTempSave controller error ======================");
			return jsonRecipeList;
		}
	}
		
	//레시피 베이스 재료 조회
	@RequestMapping(value = "/listRecipeBaseMaterial/{search_text}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.GET)
	@ResponseBody
	public String listRecipeBaseMaterial(@PathVariable("search_text") String search_text) {
		logger.info("====================== listRecipeBaseMaterial controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			List<Map.Entry<String, Object>> recipe_base_material_list = recipeRegistService.listRecipeBaseMaterial(search_text);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			map.put("recipe_base_material_list", recipe_base_material_list);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== listRecipeBaseMaterial controller end ======================");
			return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== listRecipeBaseMaterial controller error ======================");
			return jsonRecipeList;
		}
	}
	
	//레시피 업로드
	@RequestMapping(value = "/recipeUpload/{recipe_key}", produces = "application/json; charset=UTF-8", 
			method = RequestMethod.PUT)
	@ResponseBody
	public String recipeUpload(@PathVariable("recipe_key") String recipe_key) {
		logger.info("====================== recipeUpload controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipeRegistService.recipeUpload(recipe_key);
			
			map.put("response_code", "200");
			map.put("response_desc", "ok");
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeUpload controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("response_code", "500");
			map.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(map);
			
			logger.info("====================== recipeUpload controller error ======================");
	        return jsonRecipeList;
		}
	}
	
	//이미지 삭제
	@PostMapping("/recipeImageDelete")
	@ResponseBody
    public String recipeImageDelete(@RequestParam("images") String imageFileName) {
		logger.info("====================== recipeImageDelete controller start ======================");
		
		String jsonRecipeList = null;
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			recipeRegistService.recipeImageDelete(imageFileName);
			
			searchMap.put("response_code", "200");
			searchMap.put("response_desc", "ok");
			
			jsonRecipeList = gson.toJson(searchMap);
			
			logger.info("====================== recipeImageDelete controller end ======================");
	        return jsonRecipeList;
		} catch (Exception e) {
			e.printStackTrace();
			searchMap.put("response_code", "500");
			searchMap.put("response_desc", e);
			
			jsonRecipeList = gson.toJson(searchMap);
			
			logger.info("====================== recipeImageDelete controller error ======================");
			e.printStackTrace();
	        return jsonRecipeList;
		}
    }
}
