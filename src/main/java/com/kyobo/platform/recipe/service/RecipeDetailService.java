package com.kyobo.platform.recipe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.config.HttpConfig;
import com.kyobo.platform.recipe.dao.RecipeMaterial;
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
			ArrayList<RecipeMaterial> recipe_material_list = recipeDetailMapper.selectRecipeMaterial(recipe_key);
			recipe.setRecipe_material_list(recipe_material_list);
			
			// 레시피 순서 정보
			ArrayList<RecipeOrder> recipe_order_list = recipeDetailMapper.selectRecipeOrder(recipe_key);
			recipe.setRecipe_order_list(recipe_order_list);
			
			// 레시피 리뷰 정보
			HashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe_key);
			recipe.setRecipe_review_cnt_info(recipe_review_cnt_info);
			
			// 레시피 작성자 정보
			
			HttpConfig httpConfig = new HttpConfig();

			// 레시피 영양소 정보(ML 연계)
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
			
			recipe.setRecipe_nutrient_material_list(recipe_nutrient_map_list);
			
			// 레시피 섭취자제재료 정보(ML 연계)
//			JsonObject json_control_object = new JsonObject();
//			JSONObject response_control_json = null;
//			
//			json_control_object.addProperty("birthday", "birthday");
//			json_control_object.addProperty("baby_id", "baby_id");
//			
//			String control_url = "/material/restriction";
//			String control_type = "POST";
//			
//			response_control_json = httpConfig.callApi(json_control_object, control_url, control_type);
			String control_data = "{\r\n"
					+ "  \"restrictions\": [\r\n"
					+ "	  {\r\n"
					+ "		  \"material_id\": \"4000001\",\r\n"
					+ "		  \"material_name\": \"꿀\",\r\n"
					+ "		  \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg\"\r\n"
					+ "	  },\r\n"
					+ "	  {\r\n"
					+ "		  \"material_id\": \"4000002\",\r\n"
					+ "		  \"material_name\": \"사과\",\r\n"
					+ "		  \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg\"\r\n"
					+ "	  }\r\n"
					+ "  ]\r\n"
					+ "}";
			JSONParser control_parser = new JSONParser();
			JSONObject json_control_obj = (JSONObject) control_parser.parse(control_data);
			JSONArray json_control_array = (JSONArray) json_control_obj.get("restrictions");
			ArrayList<Map<String, Object>> recipe_control_map_list = new ArrayList<Map<String, Object>>();
			
			for(int i = 0; i < json_control_array.size(); i++) {
				JSONObject json_control_arr = (JSONObject) json_control_array.get(i);
				
				String material_name = json_control_arr.get("material_name").toString();
				String image_url = json_control_arr.get("image_url").toString();
				
				HashMap<String, Object> recipe_control_map = new HashMap<>();
				
				recipe_control_map.put("recipe_control_material_name", material_name);
				recipe_control_map.put("recipe_control_material_img_path", image_url);
				
				recipe_control_map_list.add(recipe_control_map);
			}
			recipe.setRecipe_control_material_list(recipe_control_map_list);
			
			// 레시피 알러지 유발재료 정보(ML 연계)
//			JsonObject json_allergy_object = new JsonObject();
//			JSONObject response_allergy_json = null;
			String recipe_allergy_material_baby_yn = "";
//			
//			json_allergy_object.addProperty("recipe_id", "recipe_id");
//			json_allergy_object.addProperty("baby_id", "baby_id");
//			
//			String allergy_url = "/material/allergy";
//			String allergy_type = "POST";
//			
//			response_allergy_json = httpConfig.callApi(json_allergy_object, allergy_url, allergy_type);
			String allergy_data = "{\r\n"
					+ "  \"allergy\": [\r\n"
					+ "	  {\r\n"
					+ "		  \"material_id\": \"4000001\",\r\n"
					+ "		  \"material_name\": \"꿀\",\r\n"
					+ "		  \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg\",\r\n"
					+ "		  \"exists\": false,\r\n"
					+ "	  },\r\n"
					+ "	  {\r\n"
					+ "		  \"material_id\": \"4000002\",\r\n"
					+ "		  \"material_name\": \"사과\",\r\n"
					+ "		  \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg\",\r\n"
					+ "		  \"exists\": false\r\n"
					+ "	  }\r\n"
					+ "  ]\r\n"
					+ "}";
			JSONParser allergy_parser = new JSONParser();
			JSONObject json_allergy_obj = (JSONObject) allergy_parser.parse(allergy_data);
			JSONArray json_allergy_array = (JSONArray) json_allergy_obj.get("allergy");
			ArrayList<Map<String, Object>> recipe_allergy_map_list = new ArrayList<Map<String, Object>>();
			
			for(int i = 0; i < json_allergy_array.size(); i++) {
				JSONObject json_allergy_arr = (JSONObject) json_allergy_array.get(i);
				
				String material_name = json_allergy_arr.get("material_name").toString();
				String image_url = json_allergy_arr.get("image_url").toString();
				String exists = json_allergy_arr.get("exists").toString();
				
				if(exists.equals("true")) {
					recipe_allergy_material_baby_yn = "Y";
				} else if(exists.equals("false")) {
					recipe_allergy_material_baby_yn = "N";
				}
				
				HashMap<String, Object> recipe_allergy_map = new HashMap<>();
				
				recipe_allergy_map.put("recipe_control_material_name", material_name);
				recipe_allergy_map.put("recipe_control_material_img_path", image_url);
				recipe_allergy_map.put("recipe_allergy_material_baby_yn", recipe_allergy_material_baby_yn);
				
				recipe_allergy_map_list.add(recipe_allergy_map);
			}
			recipe.setRecipe_allergy_material_list(recipe_allergy_map_list);
						
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
