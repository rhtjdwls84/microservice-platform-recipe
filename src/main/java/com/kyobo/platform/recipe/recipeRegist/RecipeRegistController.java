package com.kyobo.platform.recipe.recipeRegist;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.jets3t.service.CloudFrontServiceException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipeRegist")
@RestController
@RequiredArgsConstructor
public class RecipeRegistController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistController.class);
	
	private final RedisService redisService = new RedisService();
	
	private final RecipeRegistService recipeRegistService;
	
	//레시피작성
	@RequestMapping(value = "/recipeRegist", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
//	public String recipeRegist(@RequestBody RecipeRegist recipeRegist) {
	public String recipeRegist() {
		logger.info("====================== recipeRegist start ======================");
		//Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
		
		RecipeRegist recipeRegist = new RecipeRegist();
		recipeRegist.setRecipe_user_id("rhtjdwls84");
		recipeRegist.setRecipe_name("연어빠바요뜨");
		recipeRegist.setRecipe_desc("아이들이 좋아하는 레시피");
		recipeRegist.setRecipe_category("밥/죽");
		recipeRegist.setRecipe_lead_time("1시간이내");
		recipeRegist.setRecipe_level("쉬워요");
		recipeRegist.setRecipe_babyfood_step("중기 이유식");
		recipeRegist.setRecipe_main_img_name("20221031-071634982.csv");
		recipeRegist.setRecipe_main_img_path("s3://kyobo-ml/ml-rds-recipe/recipe_db_ml/RECIPE_MATERIAL_TB/");
		recipeRegist.setRecipe_health_note("건강노트입니다");
		recipeRegist.setRecipe_servings(1);
		recipeRegist.setRecipe_temp_step("완료");
		recipeRegist.setRecipe_check_status("검수대기");
		recipeRegist.setRecipe_write_status("업로드");

		ArrayList<RecipeMaterial> recipeMaterialList = new ArrayList<RecipeMaterial>();

		RecipeMaterial recipeMaterial = new RecipeMaterial();
		recipeMaterial.setRecipe_material_name("연어");
		recipeMaterial.setRecipe_material_classify("기본재료");
		recipeMaterial.setRecipe_material_main_classify("해산물");
		recipeMaterial.setRecipe_material_meature("1마리");
		recipeMaterialList.add(recipeMaterial);
		
		RecipeMaterial recipeMaterial1 = new RecipeMaterial();
		recipeMaterial1.setRecipe_material_name("양파");
		recipeMaterial1.setRecipe_material_classify("기본재료");
		recipeMaterial1.setRecipe_material_main_classify("채소류");
		recipeMaterial1.setRecipe_material_meature("1개");
		recipeMaterialList.add(recipeMaterial1);
		
		recipeRegist.setRecipe_material_arr(recipeMaterialList);

		ArrayList<RecipeOrder> recipeOrderList = new ArrayList<RecipeOrder>();

		RecipeOrder recipeOrder = new RecipeOrder();
		recipeOrder.setRecipe_order("1");
		recipeOrder.setRecipe_order_desc("연어를 자른다");
		recipeOrder.setRecipe_order_img_name("20221031-071634982.csv");
		recipeOrder.setRecipe_order_img_path("s3://kyobo-ml/ml-rds-recipe/recipe_db_ml/RECIPE_MATERIAL_TB/");
		recipeOrderList.add(0, recipeOrder);
		
		RecipeOrder recipeOrder1 = new RecipeOrder();
		recipeOrder1.setRecipe_order("2");
		recipeOrder1.setRecipe_order_desc("양파를 덮는다");
		recipeOrder1.setRecipe_order_img_name("20221031-071634982.csv");
		recipeOrder1.setRecipe_order_img_path("s3://kyobo-ml/ml-rds-recipe/recipe_db_ml/RECIPE_MATERIAL_TB/");
		recipeOrderList.add(recipeOrder1);
		
		recipeRegist.setRecipe_order_arr(recipeOrderList);
		
		String jsonRecipeList = null;
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
//		if(redisUser != null) {
			//목록 조회
			try {
				String recipe_key = recipeRegistService.insertRecipe(recipeRegist);
				
				searchMap.put("response_code", "200");
				searchMap.put("response_desc", "ok");
				searchMap.put("recipe_key", recipe_key);
				
			} catch (Exception e) {
				searchMap.put("response_code", "500");
				searchMap.put("response_desc", e);
			}
//		}
			Gson gson = new Gson();
			jsonRecipeList = gson.toJson(searchMap);
		logger.info("====================== recipeRegist end ======================");
        return jsonRecipeList;     
        
    }
	
	@PostMapping("/recipeImageUpload")
	@ResponseBody
    public String recipeImageUpload(@RequestParam("images") List<MultipartFile> multipartFiles) throws IOException, ParseException, CloudFrontServiceException {
		logger.info("====================== recipeImageUpload start ======================");
		String jsonRecipeList = null;
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		
		try {
			List<String> signedUrls = recipeRegistService.recipeImageUpload(multipartFiles);
			
			searchMap.put("response_code", "200");
			searchMap.put("response_desc", "ok");
			searchMap.put("imageUrls", signedUrls);
		} catch (Exception e) {
			searchMap.put("response_code", "500");
			searchMap.put("response_desc", e);
		}
		Gson gson = new Gson();
		jsonRecipeList = gson.toJson(searchMap);
		logger.info("====================== recipeImageUpload end ======================");
        return jsonRecipeList;
    }
	
//	//검색 레시피 목록 조회
//	@RequestMapping(value = "/searchRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	@GetMapping("/searchRecipeList/{search_text}/{search_type}")
//	@Cacheable(value = "post-single", key = "#user_id", cacheManager = "cacheManager")
//    public String searchRecipeList(@RequestParam(value = "search_text", required = true) String search_text, 
//    		@RequestParam(value = "search_type", required = false) String search_type) {
//		logger.info("====================== recipeList start ======================");
//		//Redis 세션체크
////			Optional<RedisUser> redisUser = redisService.redisGetSession(id);
//		
//		String jsonRecipeList = null;
////			if(redisUser != null) {
//			//목록 조회
//			List<RecipeList> recipeList = recipeListService.getSearchRecipeList(search_text, search_type);
//			Gson gson = new Gson();
//			jsonRecipeList = gson.toJson(recipeList);
////			}
//		logger.info("====================== recipeList end ======================");
//        return jsonRecipeList;     
//    }
}
