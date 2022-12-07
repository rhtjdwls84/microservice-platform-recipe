package com.kyobo.platform.recipe.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
	
	private String pre_path = "src\\main\\resources\\";
	
	private final RecipeMainMapper recipeMainMapper;
	
	public LinkedHashMap<String, Object> mainInfo() {
		logger.info("====================== mainInfo service start ======================");
		
		try {
			// 건강키워드의 경우 현재는 MVP1에서는 고정
			// 배너의 경우에는 MVP1에서 어드민 관리 테이블을 개발할 경우 DB에서 조회 개발 불가일경우 고정된 배너 형테로 전달
			// 제철재료 문구의 경우 어드민에서 관리하고 테이블을 만듬, 시스템 날짜에 준해서 주단위 재료를 테이블에서 조회
			// 다이나믹 문구는 MVP1에서는 정해진 값으로 시스템 날짜 체크해서 값 고정 시킴
			LinkedHashMap<String, Object> main_info_map = new LinkedHashMap<String, Object>();
			
			main_info_map.put("health_keywords", "[\"성장\", \"두뇌발달\", \"소화불량\", \"장염\", \"아토피\", \"감기\"]");
			main_info_map.put("dynamic_sentense", "기분좋은 주말 아침이에요");
			
//			String season_ingredient_sentence = recipeMainMapper.selectSeasonIngredientSentence();
			main_info_map.put("season_ingredient_sentence", "10월 제철 재료 감자");
			
			
//			List<Map<String, Object>> banner_list = recipeMainMapper.selectBannerList();
			ArrayList<Map<String, Object>> banner_list = new ArrayList<Map<String, Object>>();
			
			for(int i = 0; i < 3; i++) {
				LinkedHashMap<String, Object> banner_info_map = new LinkedHashMap<String, Object>();
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
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> recipeCustomBasedList(String user_id, String more_yn) {
		logger.info("====================== recipeCustomBasedList service start ======================");
		
		try {
			// 유저의 알레르기 및 건강키워드 정보 API 호출(회원 API 호출 or 세션에서 가져오기)
			
			// 로그인 사용자 맞춤 레시피(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			ArrayList<Map<String, Object>> baby_allergy_list = new ArrayList<Map<String, Object>>();
			ArrayList<Map<String, Object>> baby_concern_list = new ArrayList<Map<String, Object>>();
			
			//아기 알러지 및 관심사 정보 조회(쿼리)
			
			for(int i = 0; i < baby_allergy_list.size(); i++) {
				LinkedHashMap<String, Object> baby_allergy_map = new LinkedHashMap<>();
				baby_allergy_map.put("baby_allergy_ingredient_code", "ALG000001");
				baby_allergy_map.put("baby_allergy_ingredient_name", "우유");
				
				baby_allergy_list.add(baby_allergy_map);
			}
			
			for(int i = 0; i < baby_concern_list.size(); i++) {
				LinkedHashMap<String, Object> baby_concern_map = new LinkedHashMap<>();
				baby_concern_map.put("baby_allergy_ingredient_code", "ALG000001");
				baby_concern_map.put("baby_allergy_ingredient_name", "우유");
				
				baby_concern_list.add(baby_concern_map);
			}
			
			json_object.put("baby_key", user_id);
			json_object.put("baby_allergy", baby_allergy_list);
			json_object.put("baby_concern_tag", baby_concern_list);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/recsys/recipe/custom";
			String type = "POST";
				
			response_json = httpConfig.callApi(json_object, url, type);
//			String json_data = "{\r\n"
//				+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
//				+ "}";
//			JSONParser parser = new JSONParser();
//			JSONObject json_obj = (JSONObject) parser.parse(json_data);
			JSONArray json_array = (JSONArray) response_json.get("recipe_key");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("json_interest_array", json_array);
			map.put("more_yn", more_yn);
			map.put("type", "custom");
			
			List<Map<String, Object>> recipe_list = recipeMainMapper.selectRecipeList(map);
			
			if(recipe_list != null ) {
				for(int i = 0; i < recipe_list.size(); i++) {
					String recipe_key = recipe_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
				//작성자 정보 API 호출
			}
						
			logger.info("====================== recipeCustomBasedList service end ======================");
			return recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> recipeUserBodyBasedList(String user_id, String more_yn) {
		logger.info("====================== recipeUserBodyBasedList service start ======================");
		
		try {
			// 유저의 신체 정보 API 호출(회원 API 호출 or 세션에서 가져오기)
			
			// 로그인 사용자 건강 기반 레시피(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			
			// 아이 신체정보 조회(쿼리)
			
			LinkedHashMap<String, Object> baby_map = new LinkedHashMap<>();
			baby_map.put("gender", 0);
			baby_map.put("birthday", "2020-11-14");
			baby_map.put("height", 110.2);
			baby_map.put("weight", 28.4);
	
			json_object.put("baby_key", user_id);
			json_object.put("baby", baby_map);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/recsys/recipe/bodyinfo";
			String type = "POST";
				
			response_json = httpConfig.callApi(json_object, url, type);
//			String json_data = "{\r\n"
//					+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
//					+ "}";
//			JSONParser parser = new JSONParser();
//			JSONObject json_obj = (JSONObject) parser.parse(json_data);
			JSONArray json_array = (JSONArray) response_json.get("recipe_key");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("json_interest_array", json_array);
			map.put("more_yn", more_yn);
			map.put("type", "body");
			
			List<Map<String, Object>> recipe_list = recipeMainMapper.selectRecipeList(map);
			
			if(recipe_list != null ) {
				for(int i = 0; i < recipe_list.size(); i++) {
					String recipe_key = recipe_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
				//작성자 정보 API 호출
			}
						
			logger.info("====================== recipeUserBodyBasedList service end ======================");
			return recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> noUserBodyBasedRecipeList(String gender, String birthday, String height, String weight) {
		logger.info("====================== noUserBodyBasedRecipeList service start ======================");
		
		try {
			// 비로그인 사용자 신체 기반 레시피(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			
			LinkedHashMap<String, Object> baby_map = new LinkedHashMap<>();
			baby_map.put("gender", gender);
			baby_map.put("birthday", birthday);
			baby_map.put("height", height);
			baby_map.put("weight", weight);
	
			json_object.put("baby", baby_map);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/recsys/recipe/bodyinfo";
			String type = "POST";

			response_json = httpConfig.callApi(json_object, url, type);
//						String json_data = "{\r\n"
//								+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
//								+ "}";
//						JSONParser parser = new JSONParser();
//						JSONObject json_obj = (JSONObject) parser.parse(json_data);
			JSONArray json_array = (JSONArray) response_json.get("recipe_key");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("json_interest_array", json_array);
			
			List<Map<String, Object>> recipe_list = recipeMainMapper.selectRecipeList(map);
			
			if(recipe_list != null ) {
				for(int i = 0; i < recipe_list.size(); i++) {
					String recipe_key = recipe_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
			}
						
			logger.info("====================== noUserBodyBasedRecipeList service end ======================");
			return recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> noUserAllergyBasedRecipeList(ArrayList<Map<String, Object>> baby_allergy_list) {
		logger.info("====================== noUserAllergyBasedRecipeList service start ======================");
		
		try {
			// 비로그인 사용자 알러지 기반 레시피(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			
			for(int i = 0; i < baby_allergy_list.size(); i++) {
				LinkedHashMap<String, Object> baby_allergy_map = new LinkedHashMap<>();
				baby_allergy_map.put("baby_allergy_ingredient_code", "ALG000001");
				baby_allergy_map.put("baby_allergy_ingredient_name", "우유");
				
				baby_allergy_list.add(baby_allergy_map);
			}
			
			json_object.put("baby_allergy", baby_allergy_list);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/recsys/recipe/custom";
			String type = "POST";
				
			response_json = httpConfig.callApi(json_object, url, type);
//									String json_data = "{\r\n"
//										+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
//										+ "}";
//									JSONParser parser = new JSONParser();
//									JSONObject json_obj = (JSONObject) parser.parse(json_data);
			JSONArray json_array = (JSONArray) response_json.get("recipe_key");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("json_interest_array", json_array);
			
			List<Map<String, Object>> recipe_list = recipeMainMapper.selectRecipeList(map);
			
			if(recipe_list != null ) {
				for(int i = 0; i < recipe_list.size(); i++) {
					String recipe_key = recipe_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
				//작성자 정보 API 호출
			}
						
			logger.info("====================== noUserAllergyBasedRecipeList service end ======================");
			return recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> noUserHealthBasedRecipeList(ArrayList<Map<String, Object>> baby_concern_list) {
		logger.info("====================== noUserHealthBasedRecipeList service start ======================");
		
		try {
			// 비로그인 사용자 맞춤 레시피(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			
			for(int i = 0; i < baby_concern_list.size(); i++) {
				LinkedHashMap<String, Object> baby_concern_map = new LinkedHashMap<>();
				baby_concern_map.put("baby_allergy_ingredient_code", "ALG000001");
				baby_concern_map.put("baby_allergy_ingredient_name", "우유");
				
				baby_concern_list.add(baby_concern_map);
			}
			
			json_object.put("baby_concern_tag", baby_concern_list);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/recsys/recipe/custom";
			String type = "POST";
				
			response_json = httpConfig.callApi(json_object, url, type);
//									String json_data = "{\r\n"
//										+ "  \"recipe_ids\": [\"1000003\", \"1000004\", \"1000011\"]\r\n"
//										+ "}";
//									JSONParser parser = new JSONParser();
//									JSONObject json_obj = (JSONObject) parser.parse(json_data);
			JSONArray json_array = (JSONArray) response_json.get("recipe_key");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("json_interest_array", json_array);
			
			List<Map<String, Object>> recipe_list = recipeMainMapper.selectRecipeList(map);
			
			if(recipe_list != null ) {
				for(int i = 0; i < recipe_list.size(); i++) {
					String recipe_key = recipe_list.get(i).get("recipe_key").toString();
					int recipe_review_count = recipeMainMapper.selectRecipeReviewCount(recipe_key);
					
					recipe_list.get(i).put("recipe_review_total_count", recipe_review_count);
				}
				//작성자 정보 API 호출
			}
						
			logger.info("====================== noUserHealthBasedRecipeList service end ======================");
			return recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<Map<String, Object>> seasonIngredientBasedRecipeList(String season_ingredient, String more_yn) throws ParseException {
		logger.info("====================== seasonIngredientBasedRecipeList service start ======================");
		
		try {
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("season_ingredient", season_ingredient);
			map.put("more_yn", more_yn);
			
			List<Map<String, Object>> season_ingredient_based_recipe_list = recipeMainMapper.seasonIngredientBasedRecipeList(map);
			
			logger.info("====================== seasonIngredientBasedRecipeList service end ======================");
			return season_ingredient_based_recipe_list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> userBabyInfo(String user_id) {
		logger.info("====================== userBabyInfo service start ======================");
		
		try {
			// 유저의 아이에 대한 정보 가져오기(회원 API 호출 or 세션에서 가져오기)
			
			// 아이 정보(ML 연계)
			HttpConfig httpConfig = new HttpConfig();
			JSONObject json_object = new JSONObject();
			JSONObject response_json = null;
			
			LinkedHashMap<String, Object> baby_map = new LinkedHashMap<>();
			baby_map.put("gender", 0);
			baby_map.put("birthday", "2020-11-14");
			baby_map.put("height", 110.2);
			baby_map.put("weight", 28.4);
	
			json_object.put("baby_key", user_id);
			json_object.put("baby", baby_map);
			
			Properties properties = new Properties();
	        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
	        
	        String ml_url = properties.getProperty("mlurl");
			String url = ml_url + "/infant/profile";
			String type = "POST";
				
			response_json = httpConfig.callApi(json_object, url, type);
//			String json_data = "{\r\n"
//					+ "  \"baby_key\": \"000001\",\r\n"
//					+ "  \"profile\": {\r\n"
//					+ "    \"day\": 630,\r\n"
//					+ "    \"month\": 20,\r\n"
//					+ "    \"rank\": 50,\r\n"
//					+ "    \"statement\": \"다행이예요! 같은 또래 중 50등이예요\"\r\n"
//					+ "  },  \r\n"
//					+ "  \"profile_height\": {\r\n"
//					+ "    \"percentage\": 10,\r\n"
//					+ "    \"statement\": \"하위 10%\" \r\n"
//					+ "  }, \r\n"
//					+ "  \"profile_weight\": {\r\n"
//					+ "    \"percentage\": 60,\r\n"
//					+ "    \"statement\": \"상위 40%\" \r\n"
//					+ "  },\r\n"
//					+ "  \"profile_bmi\": {\r\n"
//					+ "    \"percentage\": 25,\r\n"
//					+ "    \"statement\": \"저체중\" \r\n"
//					+ "  },\r\n"
//					+ "  \"profile_dashboard\": {\r\n"
//					+ "    \"energy\": 1000,\r\n"
//					+ "    \"energy_statement\" : \"1,000kcal\"\r\n"
//					+ "  }\r\n"
//					+ "}";
//			JSONParser parser = new JSONParser();
//			JSONObject json_obj = (JSONObject) parser.parse(json_data);
			LinkedHashMap<String, Object> baby_info_map = new LinkedHashMap<String, Object>();
			
			baby_info_map.put("baby_key", user_id);
			baby_info_map.put("profile", response_json.get("profile"));
			baby_info_map.put("profile_height", response_json.get("profile_height"));
			baby_info_map.put("profile_weight", response_json.get("profile_weight"));
			baby_info_map.put("profile_bmi", response_json.get("profile_bmi"));
			baby_info_map.put("profile_dashboard", response_json.get("profile_dashboard"));
						
			logger.info("====================== userBabyInfo service end ======================");
			return baby_info_map;
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalExceptionHandler();
		}
	}
}
