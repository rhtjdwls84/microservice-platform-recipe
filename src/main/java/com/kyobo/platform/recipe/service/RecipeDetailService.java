package com.kyobo.platform.recipe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeDetailMapper recipeDetailMapper;
	
	public Recipe recipeDetail(String recipe_key) throws ParseException {
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
			HashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe_key);
			recipe.setRecipe_review_cnt_info(recipe_review_cnt_info);
			
			// 레시피 태그 정보
			List<Map.Entry<String, Object>> recipe_tag_list = recipeDetailMapper.selectRecipeTag(recipe_key);
			recipe.setRecipe_tag_list(recipe_tag_list);
			
			// 레시피 작성자 정보
			
			HttpConfig httpConfig = new HttpConfig();

			// 레시피 영양소, 섭취자제 재료ㅕ 정보(ML 연계)
//			JsonObject json_nutrient_object = new JsonObject();
//			JSONObject response_nutrient_json = null;
//			
//			json_nutrient_object.addProperty("recipe_id", "recipe_id");
//			json_nutrient_object.addProperty("birthday", "birthday");
//			json_nutrient_object.addProperty("baby_id", "baby_id");
//			
//			String nutrient_url = "/nutrients";
//			String nutrient_type = "POST";
			
//			
//			response_nutrient_json = httpConfig.callApi(json_nutrient_object, nutrient_url, nutrient_type);
			String nutrient_data = "{\r\n"
					+ "  \"representative_nutrients\" : [\r\n"
					+ "	{\r\n"
					+ "      \"nutrients_id\": \"5001\",\r\n"
					+ "      \"nutrients_name\": \"탄수화물\",\r\n"
					+ "      \"recommended\": 50\r\n"
					+ "    },\r\n"
					+ "	{\r\n"
					+ "      \"nutrients_id\": \"5002\",\r\n"
					+ "      \"nutrients_name\": \"지방\",\r\n"
					+ "      \"recommended\": 30\r\n"
					+ "    }\r\n"
					+ "  ],\r\n"
					+ "  \"sugars\": 30,\r\n"
					+ "  \"salt\": 20\r\n"
					+ "}";
			JSONParser nutrient_parser = new JSONParser();
			JSONObject json_nutrient_obj = (JSONObject) nutrient_parser.parse(nutrient_data);
			JSONArray json_nutrient_array = (JSONArray) json_nutrient_obj.get("representative_nutrients");
			ArrayList<Map<String, Object>> recipe_nutrient_map_list = new ArrayList<Map<String, Object>>();
			
			for(int i = 0; i < json_nutrient_array.size(); i++) {
				JSONObject json_nutrient_arr = (JSONObject) json_nutrient_array.get(i);
				
				String nutrients_name = json_nutrient_arr.get("nutrients_name").toString();
				String recommended = json_nutrient_arr.get("recommended").toString();
				
				HashMap<String, Object> recipe_nutrient_map = new HashMap<>();
				
				recipe_nutrient_map.put("recipe_nutrient_name", nutrients_name);
				recipe_nutrient_map.put("recipe_nutrient_quantity", recommended);
				
				recipe_nutrient_map_list.add(recipe_nutrient_map);
			}
			
			HashMap<String, Object> recipe_sugars_map = new HashMap<>();
			recipe_sugars_map.put("recipe_nutrient_name", "sugars");
			recipe_sugars_map.put("recipe_nutrient_quantity", json_nutrient_obj.get("sugars"));
			
			recipe_nutrient_map_list.add(recipe_sugars_map);
			
			HashMap<String, Object> recipe_salt_map = new HashMap<>();
			recipe_salt_map.put("recipe_nutrient_name", "salt");
			recipe_salt_map.put("recipe_nutrient_quantity", json_nutrient_obj.get("salt"));
			
			recipe_nutrient_map_list.add(recipe_salt_map);
			
			recipe.setRecipe_nutrient_ingredient_list(recipe_nutrient_map_list);
			
			logger.info("====================== recipeDetail service end ======================");
			return recipe;
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public HashMap<RecipeReview, Object> registRecipeReview(Recipe recipe) {
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
			HashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe.getRecipe_key());
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
