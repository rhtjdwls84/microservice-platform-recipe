package com.kyobo.platform.recipe.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.config.HttpConfig;
import com.kyobo.platform.recipe.dao.RecipeIngredient;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.RecipeReview;
import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.mapper.RecipeDetailMapper;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeDetailService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeDetailService.class);
	
	private final RecipeDetailMapper recipeDetailMapper;
	
	private String pre_path = "src\\main\\resources\\";
	
	@SuppressWarnings("unchecked")
	public Recipe recipeDetail(String recipe_key) throws ParseException, FileNotFoundException, IOException {
		logger.info("====================== recipeDetail service start ======================");
		
		// 레시피 조회수 업데이트
		int result = recipeDetailMapper.updateRecipeSearchCnt(recipe_key);
		
		if(result > 0) {
			// 레시피 상세 정보
			Recipe recipe = recipeDetailMapper.selectRecipeDetail(recipe_key);
			
			// 레시피 재료 정보
			ArrayList<RecipeIngredient> recipe_ingredient_list = recipeDetailMapper.selectRecipeIngredient(recipe_key);
			recipe.setRecipe_ingredient_list(recipe_ingredient_list);
			
			// 레시피 순서 정보
			ArrayList<RecipeOrder> recipe_order_list = recipeDetailMapper.selectRecipeOrder(recipe_key);
			recipe.setRecipe_order_list(recipe_order_list);
			
			// 레시피 리뷰 정보
			LinkedHashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe_key);
			recipe.setRecipe_review_cnt_info(recipe_review_cnt_info);
			
			// 레시피 태그 정보
			List<Map.Entry<String, Object>> recipe_tag_list = recipeDetailMapper.selectRecipeTag(recipe_key);
			recipe.setRecipe_tag_list(recipe_tag_list);
			
			// 레시피 작성자 정보
			
			// 레시피 영양소, 섭취자제 재료 정보(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			ArrayList<Map<String, Object>> recipe_arr_list = new ArrayList<Map<String, Object>>();
			ArrayList<Map<String, Object>> baby_allergy_list = new ArrayList<Map<String, Object>>();
			
			for(int i = 0; i < recipe_ingredient_list.size(); i++) {
				LinkedHashMap<String, Object> recipe_map = new LinkedHashMap<String, Object>();
				
				recipe_map.put("recipe_ingredient_key", recipe_ingredient_list.get(i).getRecipe_ingredient_key());
				recipe_map.put("recipe_ingredient_name", recipe_ingredient_list.get(i).getRecipe_ingredient_name());
				recipe_map.put("recipe_ingredient_category", recipe_ingredient_list.get(i).getRecipe_ingredient_category());
				recipe_map.put("recipe_ingredient_search_categoy", recipe_ingredient_list.get(i).getRecipe_ingredient_main_category());
				recipe_map.put("recipe_ingredient_amount", recipe_ingredient_list.get(i).getRecipe_ingredient_amount());
				recipe_map.put("recipe_ingreident_countunit", recipe_ingredient_list.get(i).getRecipe_ingredient_countunit());
				
				recipe_arr_list.add(recipe_map);
			}
			
			LinkedHashMap<String, Object> baby_map = new LinkedHashMap<>();
			baby_map.put("gender", 0);
			baby_map.put("birthday", "2020-11-14");
			baby_map.put("height", 110.2);
			baby_map.put("weight", 28.4);
			
			for(int i = 0; i < 1; i++) {
				LinkedHashMap<String, Object> baby_allergy_map = new LinkedHashMap<>();
				
				baby_allergy_map.put("baby_allergy_ingredient_code", "00");
				baby_allergy_map.put("baby_allergy_ingredient_name", "우유");
				
				baby_allergy_list.add(baby_allergy_map);
			}
			
			json_object.put("recipe_key", recipe.getRecipe_key());
			json_object.put("recipe_servingsize", recipe.getRecipe_servings());
			json_object.put("created_datetime", recipe.getCreated_datetime());
			json_object.put("last_modified_datetime", recipe.getLast_modified_datetime());
			json_object.put("recipe_ingredient", recipe_arr_list);
			json_object.put("baby_key", "baby000001");
			json_object.put("baby", baby_map);
			json_object.put("baby_allergy", baby_allergy_list);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/recipe/content";
			String type = "POST";
			
			
			response_json = httpConfig.callApi(json_object, url, type);
//			String json_data = "{\r\n"
//					+ "  \"recipe_key\": \"2000001\",\r\n"
//					+ "  \"baby_key\": \"2000001\",\r\n"
//					+ "  \"restriction\": [{\r\n"
//					+ "      \"ingredient_code\": \"4000001\",\r\n"
//					+ "      \"ingredient_name\": \"꿀\",\r\n"
//					+ "      \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg\"\r\n"
//					+ "  }],\r\n"
//					+ "  \"allergy\": [{\r\n"
//					+ "      \"ingredient_code\": \"4000001\",\r\n"
//					+ "      \"ingredient_name\": \"꿀\"\r\n"
//					+ "      \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg\",\r\n"
//					+ "      \"cause\": false\r\n"
//					+ "  }],\r\n"
//					+ "  \"nutrient\" : [{\r\n"
//					+ "      \"nutrient_code\": \"5001\",\r\n"
//					+ "      \"nutrient_name\": \"탄수화물\",\r\n"
//					+ "      \"nutrient_intake\": 50\r\n"
//					+ "  }],\r\n"
//					+ "  \"sugar\": 30,\r\n"
//					+ "  \"natrium\": 20\r\n"
//					+ "}";
//			JSONParser parser = new JSONParser();
//			JSONObject json_obj = (JSONObject) parser.parse(json_data);
			
//			JSONArray json_restriction_array = (JSONArray) response_json.get("restriction");
//			JSONArray json_allergy_array = (JSONArray) response_json.get("allergy");
//			JSONArray json_nutrient_array = (JSONArray) response_json.get("nutrient");
//			ArrayList<Map<String, Object>> recipe_restriction_map_list = new ArrayList<Map<String, Object>>();
//			ArrayList<Map<String, Object>> recipe_allergy_map_list = new ArrayList<Map<String, Object>>();
//			ArrayList<Map<String, Object>> recipe_nutrient_map_list = new ArrayList<Map<String, Object>>();
//
//			for(int i = 0; i < json_restriction_array.size(); i++) {
//				JSONObject json_restriction_arr = (JSONObject) json_restriction_array.get(i);
//				
//				String ingredient_name = json_restriction_arr.get("ingredient_name").toString();
//				String image_url = json_restriction_arr.get("image_url").toString();
//				
//				LinkedHashMap<String, Object> recipe_map = new LinkedHashMap<>();
//				
//				recipe_map.put("recipe_restriction_name", ingredient_name);
//				recipe_map.put("recipe_restriction_img_path", image_url);
//				
//				recipe_restriction_map_list.add(recipe_map);
//			}
//			recipe.setRecipe_restriction_list(recipe_restriction_map_list);
//			
//			for(int i = 0; i < json_allergy_array.size(); i++) {
//				JSONObject json_allergy_arr = (JSONObject) json_allergy_array.get(i);
//				
//				String ingredient_name = json_allergy_arr.get("ingredient_name").toString();
//				String image_url = json_allergy_arr.get("image_url").toString();
//				
//				LinkedHashMap<String, Object> recipe_map = new LinkedHashMap<>();
//				
//				recipe_map.put("recipe_allergy_name", ingredient_name);
//				recipe_map.put("recipe_allergy_img_path", image_url);
//				
//				recipe_allergy_map_list.add(recipe_map);
//			}
//			recipe.setRecipe_allergy_list(recipe_allergy_map_list);
//			
//			for(int i = 0; i < json_nutrient_array.size(); i++) {
//				JSONObject json_nutrient_arr = (JSONObject) json_nutrient_array.get(i);
//				
//				String nutrient_name = json_nutrient_arr.get("nutrient_name").toString();
//				String nutrient_intake = json_nutrient_arr.get("nutrient_intake").toString();
//				
//				LinkedHashMap<String, Object> recipe_map = new LinkedHashMap<>();
//				
//				recipe_map.put("recipe_nutrient_name", nutrient_name);
//				recipe_map.put("recipe_nutrient_quantity", nutrient_intake);
//				
//				recipe_nutrient_map_list.add(recipe_map);
//			}
//			
//			LinkedHashMap<String, Object> recipe_sugar_map = new LinkedHashMap<>();
//			recipe_sugar_map.put("recipe_nutrient_name", "sugar");
//			recipe_sugar_map.put("recipe_nutrient_quantity", response_json.get("sugar"));
//			
//			recipe_nutrient_map_list.add(recipe_sugar_map);
//			
//			LinkedHashMap<String, Object> recipe_natrium_map = new LinkedHashMap<>();
//			recipe_natrium_map.put("recipe_nutrient_name", "natrium");
//			recipe_natrium_map.put("recipe_nutrient_quantity", response_json.get("natrium"));
//			
//			recipe_nutrient_map_list.add(recipe_natrium_map);
//			
//			recipe.setRecipe_nutrient_list(recipe_nutrient_map_list);
			
			logger.info("====================== recipeDetail service end ======================");
			return recipe;
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public LinkedHashMap<RecipeReview, Object> registRecipeReview(Recipe recipe) {
		logger.info("====================== registRecipeReview service start ======================");
		
		// 레시피 선택한 리뷰 insert
		String[] recipe_select_review = recipe.getRecipe_select_review().split(",");
		int result = 0;
		
		for(int i = 0; i < recipe_select_review.length; i++) {
			recipe.setRecipe_select_review(recipe_select_review[i]);
			result = recipeDetailMapper.insertRecipeReview(recipe);
			result++;
		}
		
		if(result > 0) {
			// 레시피 리뷰수 정보 리턴
			LinkedHashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe.getRecipe_key());
			recipe.setRecipe_review_cnt_info(recipe_review_cnt_info);
			
			logger.info("====================== registRecipeReview service end ======================");
			return recipe_review_cnt_info;
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public void registRecipeScrap(Recipe recipe) {
		logger.info("====================== registRecipeScrap service start ======================");
		
		// 스크랩 등록일 경우 insert 및 레시피 스크랩수 + 1
		if(recipe.getRecipe_scrap_yn().equals("Y")) {
			int result = recipeDetailMapper.insertRecipeScrap(recipe);
			if(result > 0) {
				recipeDetailMapper.updateRecipeScrapPlus(recipe.getRecipe_key());
			}
		// 스크랩 취소일 경우 N으로 업데이트 및 레시피 스크랩수 - 1	
		} else if(recipe.getRecipe_scrap_yn().equals("N")) {
			int result = recipeDetailMapper.updateRecipeScrap(recipe);
			if(result > 0) {
				recipeDetailMapper.updateRecipeScrapMinus(recipe.getRecipe_key());
			}
		} else {
			throw new GlobalExceptionHandler();
		}
	}
}
