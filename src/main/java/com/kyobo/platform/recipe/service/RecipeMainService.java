package com.kyobo.platform.recipe.service;

import java.sql.Array;
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
import com.kyobo.platform.recipe.mapper.RecipeMainMapper;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeMainService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeMainService.class);
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeMainMapper recipeMainMapper;
	
	public HashMap<String, Object> mainInfo() {
		logger.info("====================== mainInfo service start ======================");
		
		try {
			// 건강키워드의 경우 현재는 MVP1에서는 고정
			// 배너의 경우에는 MVP1에서 어드민 관리 테이블을 개발할 경우 DB에서 조회 개발 불가일경우 고정된 배너 형테로 전달
			// 제철재료 문구의 경우 어드민에서 관리하고 테이블을 만듬, 시스템 날짜에 준해서 주단위 재료를 테이블에서 조회
			// 다이나믹 문구는 MVP1에서는 정해진 값으로 시스템 날짜 체크해서 값 고정 시킴
			HashMap<String, Object> main_info_map = new HashMap<String, Object>();
			
			main_info_map.put("health_keywords", "[\"성장\", \"두뇌발달\", \"소화불량\", \"장염\", \"아토피\", \"감기\"]");
			main_info_map.put("dynamic_sentense", "기분좋은 주말 아침이에요");
			
//			String season_ingredient_sentence = recipeMainMapper.selectSeasonIngredientSentence();
			main_info_map.put("season_ingredient_sentence", "10월 제철 재료 감자");
			
			
//			List<Map<String, Object>> banner_list = recipeMainMapper.selectBannerList();
			ArrayList<Map<String, Object>> banner_list = new ArrayList<Map<String, Object>>();
			
			for(int i = 0; i < 3; i++) {
				HashMap<String, Object> banner_info_map = new HashMap<String, Object>();
				banner_info_map.put("banner_type", "이벤트");
				banner_info_map.put("banner_name", "꼬미의 몸무게에 관심을 가져주세요.");
				banner_info_map.put("baby_weight_text", "이 시기의 몸무게는 키 성좡과 관련이 높아요. 성장에 도움이 되는 단백질과 철분이 많은 레시피를 알아볼까요?");
				banner_info_map.put("banner_img_path", "http://d3am0bqv86scod.cloudfront.net/0bc30422-ecb7-45b1-9505-b0b954bfee9e-JIN07742.jpg?Expires=1605392400&Signature=A7lATLrD-bhgiWSkNpz~LfPbCg5-M61mRBhVixLPnRD9wQkHBY1m6G8Cjv88Bg1xcnIq36xOL61qT95GaqfHc~AodPXiI4ZHz4i30tfQrvKGgNEfaj5hEeiEHcTHsmMLIkxFZd9OUNJhJW9pvIrbrKSIOFCzBmuGb0gAcf0NNcdPwuahSl~DdATIbN2A7gN6gCBoDBpKitBKvPSmHqmAR7iEx4jzahFS8hNaumvlIgLSUQxE~7BAG~BID7I2MjAPJYA4vaPr1B~AZJFzkhpINDQaPnHjoWf02lagBFMBoFVtsLStJuBdHG7O1LtSfMTXcviafrDlDgV7KgwByLIHEA__&Key-Pair-Id=APKAZ3MKOJFETMDPAWV6");
				
				banner_list.add(banner_info_map);
			}
			main_info_map.put("banner_list", banner_list);
			
			logger.info("====================== mainInfo service end ======================");
			return main_info_map;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> recipeCustomBasedList(String user_id) {
		logger.info("====================== recipeCustomBasedList service start ======================");
		
		try {
			// 유저의 알레르기 및 건강키워드 정보 API 호출(회원 API 호출 or 세션에서 가져오기)
			
			HttpConfig httpConfig = new HttpConfig();
		
			// 레시피 영양소 정보(ML 연계)
		//		JsonObject json_custom_based_object = new JsonObject();
		//		JSONObject response_custom_based_json = null;
		//		
		//		json_custom_based_object.addProperty("health_keywords", "health_keywords");
		//		json_custom_based_object.addProperty("allergy", "allergy");
		//		json_custom_based_object.addProperty("baby_id", "baby_id");
		//		
		//		String custom_based_url = "/recipe/suggestion/interest";
		//		String custom_based_type = "POST";
		//		
		//		response_custom_based_json = httpConfig.callApi(json_custom_based_object, custom_based_url, custom_based_type);
			String custom_based_data = "{\r\n"
					+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
					+ "}";
			JSONParser custom_based_parser = new JSONParser();
			JSONObject json_custom_based_obj = (JSONObject) custom_based_parser.parse(custom_based_data);
			JSONArray json_custom_based_array = (JSONArray) json_custom_based_obj.get("recipe_ids");
			HashMap<String, Object> custom_based_map = new HashMap<String, Object>();
			custom_based_map.put("json_interest_array", json_custom_based_array);
			
			List<Map<String, Object>> recipe_custom_based_list = recipeMainMapper.selectRecipeList(custom_based_map);
			
			if(recipe_custom_based_list != null ) {
				for(int i = 0; i < recipe_custom_based_list.size(); i++) {
					String recipe_key = recipe_custom_based_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_custom_based_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
			}
						
			logger.info("====================== recipeCustomBasedList service end ======================");
			return recipe_custom_based_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> recipeUserBasedList(String user_id) {
		logger.info("====================== recipeUserBasedList service start ======================");
		
		try {
			// 유저의 신체 정보 API 호출(회원 API 호출 or 세션에서 가져오기)
			
			HttpConfig httpConfig = new HttpConfig();
		
			// 레시피 영양소 정보(ML 연계)
		//		JsonObject json_user_based_object = new JsonObject();
		//		JSONObject response_user_based_json = null;
		//		
		//		json_user_based_object.addProperty("birthday", "birthday");
		//		json_user_based_object.addProperty("sex", "sex");
		//		json_user_based_object.addProperty("height", "height");
		//		json_user_based_object.addProperty("weight", "weight");
		//		json_user_based_object.addProperty("baby_id", "baby_id");
		//		
		//		String user_based_url = "/recipe/suggestion/growth";
		//		String user_based_type = "POST";
		//		
		//		response_user_based_json = httpConfig.callApi(json_user_based_object, user_based_url, user_based_type);
			String user_based_data = "{\r\n"
					+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
					+ "}";
			JSONParser user_based_parser = new JSONParser();
			JSONObject json_user_based_obj = (JSONObject) user_based_parser.parse(user_based_data);
			JSONArray json_user_based_array = (JSONArray) json_user_based_obj.get("recipe_ids");
			HashMap<String, Object> user_based_map = new HashMap<String, Object>();
			user_based_map.put("json_interest_array",json_user_based_array);
			
			List<Map<String, Object>> recipe_user_based_list = recipeMainMapper.selectRecipeList(user_based_map);
			
			if(recipe_user_based_list != null ) {
				for(int i = 0; i < recipe_user_based_list.size(); i++) {
					String recipe_key = recipe_user_based_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_user_based_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
				
				//작성자 정보 API 호출
			}
						
			logger.info("====================== recipeUserBasedList service end ======================");
			return recipe_user_based_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> noUserCustomBasedRecipeList(String age, String sex, String height, String weight) {
		logger.info("====================== noUserCustomBasedRecipeList service start ======================");
		
		try {
			HttpConfig httpConfig = new HttpConfig();
		
			// 레시피 영양소 정보(ML 연계)
		//		JsonObject json_no_user_custom_based_object = new JsonObject();
		//		JSONObject response_no_user_custom_based_json = null;
		//		
		//		json_no_user_custom_based_object.addProperty("birthday", "age");
		//		json_no_user_custom_based_object.addProperty("sex", "sex");
		//		json_no_user_custom_based_object.addProperty("height", "height");
		//		json_no_user_custom_based_object.addProperty("weight", "weight");
		//		json_no_user_custom_based_object.addProperty("baby_id", "baby_id");
		//		
		//		String no_user_custom_based_url = "/recipe/suggestion/growth";
		//		String no_user_custom_based_type = "POST";
		//		
		//		response_no_user_custom_based_json = 
		//		httpConfig.callApi(json_no_user_custom_based_object, no_user_custom_based_url, no_user_custom_based_type);
			String no_user_custom_based_data = "{\r\n"
					+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
					+ "}";
			JSONParser no_user_custom_based_parser = new JSONParser();
			JSONObject json_no_user_custom_based_obj = (JSONObject) no_user_custom_based_parser.parse(no_user_custom_based_data);
			JSONArray json_no_user_custom_based_array = (JSONArray) json_no_user_custom_based_obj.get("recipe_ids");
			HashMap<String, Object> no_user_custom_based_map = new HashMap<String, Object>();
			no_user_custom_based_map.put("json_interest_array",json_no_user_custom_based_array);
			
			List<Map<String, Object>> recipe_no_user_custom_based_list = recipeMainMapper.selectRecipeList(no_user_custom_based_map);
			
			if(recipe_no_user_custom_based_list != null ) {
				for(int i = 0; i < recipe_no_user_custom_based_list.size(); i++) {
					String recipe_key = recipe_no_user_custom_based_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_no_user_custom_based_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
			}
						
			logger.info("====================== noUserCustomBasedRecipeList service end ======================");
			return recipe_no_user_custom_based_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> noUserAllergyBasedRecipeList(Array allergy) {
		logger.info("====================== noUserAllergyBasedRecipeList service start ======================");
		
		try {
			HttpConfig httpConfig = new HttpConfig();
		
			// 레시피 영양소 정보(ML 연계)
		//		JsonObject json_no_user_allergy_based_object = new JsonObject();
		//		JSONObject response_no_user_allergy_based_json = null;
		//		
		//		json_no_user_allergy_based_object.addProperty("allergy", allergy);
		//		
		//		String no_user_allergy_based_url = "/recipe/suggestion/interest";
		//		String no_user_allergy_based_type = "POST";
		//		
		//		response_no_user_allergy_based_json = 
		//		httpConfig.callApi(json_no_user_allergy_based_object, no_user_allergy_based_url, no_user_allergy_based_type);
			String no_user_allergy_based_data = "{\r\n"
					+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
					+ "}";
			JSONParser no_user_allergy_based_parser = new JSONParser();
			JSONObject json_no_user_allergy_based_obj = (JSONObject) no_user_allergy_based_parser.parse(no_user_allergy_based_data);
			JSONArray json_no_user_allergy_based_array = (JSONArray) json_no_user_allergy_based_obj.get("recipe_ids");
			HashMap<String, Object> no_user_allergy_based_map = new HashMap<String, Object>();
			no_user_allergy_based_map.put("json_interest_array", json_no_user_allergy_based_array);
			
			List<Map<String, Object>> recipe_no_user_allergy_based_list = recipeMainMapper.selectRecipeList(no_user_allergy_based_map);
			
			if(recipe_no_user_allergy_based_list != null ) {
				for(int i = 0; i < recipe_no_user_allergy_based_list.size(); i++) {
					String recipe_key = recipe_no_user_allergy_based_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_no_user_allergy_based_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
			}
						
			logger.info("====================== noUserAllergyBasedRecipeList service end ======================");
			return recipe_no_user_allergy_based_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> noUserHealthBasedRecipeList(Array health_keywords) {
		logger.info("====================== noUserHealthBasedRecipeList service start ======================");
		
		try {
			HttpConfig httpConfig = new HttpConfig();
		
			// 레시피 영양소 정보(ML 연계)
		//		JsonObject json_no_user_health_based_object = new JsonObject();
		//		JSONObject response_no_user_health_based_json = null;
		//		
		//		json_no_user_health_based_object.addProperty("health_keywords", health_keywords);
		//		
		//		String no_user_health_based_url = "/recipe/suggestion/interest";
		//		String no_user_health_based_type = "POST";
		//		
		//		response_no_user_health_based_json = 
		//		httpConfig.callApi(json_no_user_health_based_object, no_user_health_based_url, no_user_health_based_type);
			String no_user_health_based_data = "{\r\n"
					+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
					+ "}";
			JSONParser no_user_health_based_parser = new JSONParser();
			JSONObject json_no_user_health_based_obj = (JSONObject) no_user_health_based_parser.parse(no_user_health_based_data);
			JSONArray json_no_user_health_based_array = (JSONArray) json_no_user_health_based_obj.get("recipe_ids");
			HashMap<String, Object> no_user_health_based_map = new HashMap<String, Object>();
			no_user_health_based_map.put("json_interest_array",json_no_user_health_based_array);
			
			List<Map<String, Object>> recipe_no_user_health_based_list = recipeMainMapper.selectRecipeList(no_user_health_based_map);
			
			if(recipe_no_user_health_based_list != null ) {
				for(int i = 0; i < recipe_no_user_health_based_list.size(); i++) {
					String recipe_key = recipe_no_user_health_based_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_no_user_health_based_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
			}
						
			logger.info("====================== noUserHealthBasedRecipeList service end ======================");
			return recipe_no_user_health_based_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> seasonIngredientBasedRecipeList(String season_ingredient) throws ParseException {
		logger.info("====================== seasonIngredientBasedRecipeList service start ======================");
		
		try {
			List<Map<String, Object>> season_ingredient_based_recipe_list = recipeMainMapper.seasonIngredientBasedRecipeList(season_ingredient);
			
			logger.info("====================== seasonIngredientBasedRecipeList service end ======================");
			return season_ingredient_based_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public HashMap<String, Object> userBabyInfo(String user_id) {
		logger.info("====================== userBabyInfo service start ======================");
		
		try {
			// 유저의 아이에 대한 정보 가져오기(회원 API 호출 or 세션에서 가져오기)
			
			HttpConfig httpConfig = new HttpConfig();
		
			// 레시피 영양소 정보(ML 연계)
		//		JsonObject json_baby_info_object = new JsonObject();
		//		JSONObject response_baby_info_json = null;
		//		
		//		json_baby_info_object.addProperty("birthday", birthday);
		//		json_baby_info_object.addProperty("sex", sex);
		//		json_baby_info_object.addProperty("height", height);
		//		json_baby_info_object.addProperty("weight", weight);
		//		json_baby_info_object.addProperty("baby_id", baby_id);
		//		
		//		String interest_url = "/baby/growth";
		//		String interest_type = "POST";
		//		
		//		response_baby_info_json = httpConfig.callApi(json_baby_info_object, baby_info_url, baby_info_type);
			String baby_info_data = "{\r\n"
					+ "  \"days\": 630,\r\n"
					+ "  \"months\": 20,\r\n"
					+ "  \"growth_statement\": \"다행이예요! 같은 또래 중 50등이예요\",\r\n"
					+ "  \"height\": {\r\n"
					+ "    \"ratio\": 10,\r\n"
					+ "    \"text\": \"하위 10%\" \r\n"
					+ "  }, \r\n"
					+ "  \"weight\": {\r\n"
					+ "    \"ratio\": 60,\r\n"
					+ "    \"text\": \"상위 40%\" \r\n"
					+ "  },\r\n"
					+ "  \"state\": 1,\r\n"
					+ "  \"required_energy\" : 600\r\n"
					+ "}";
			JSONParser baby_info_parser = new JSONParser();
			JSONObject json_baby_info_obj = (JSONObject) baby_info_parser.parse(baby_info_data);
			HashMap<String, Object> baby_info_map = new HashMap<String, Object>();
			
			baby_info_map.put("baby_name", "baby_name"); //회원 API에서 받은 값으로 세팅
			baby_info_map.put("baby_days", json_baby_info_obj.get("days"));
			baby_info_map.put("baby_months", json_baby_info_obj.get("months"));
			baby_info_map.put("baby_growth_statement", json_baby_info_obj.get("growth_statement"));
			
			JSONObject json_height_obj = (JSONObject) baby_info_parser.parse((String) json_baby_info_obj.get("height"));
			baby_info_map.put("baby_height", "baby_height");//회원 API에서 받은 값으로 세팅
			baby_info_map.put("baby_height_ratio", json_height_obj.get("ratio"));
			baby_info_map.put("baby_height_text", json_height_obj.get("text"));
			
			JSONObject json_weight_obj = (JSONObject) baby_info_parser.parse((String) json_baby_info_obj.get("weight"));
			baby_info_map.put("baby_weight", json_weight_obj.get("weight"));//회원 API에서 받은 값으로 세팅
			baby_info_map.put("baby_weight_ratio", json_weight_obj.get("ratio"));
			baby_info_map.put("baby_weight_text", json_weight_obj.get("text"));
			
			baby_info_map.put("baby_weight_state", json_baby_info_obj.get("state"));
			baby_info_map.put("baby_required_energy", json_baby_info_obj.get("required_energy"));
						
			logger.info("====================== userBabyInfo service end ======================");
			return baby_info_map;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
}
