package com.kyobo.platform.recipe.recipeRegist;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipe")
@RestController
@RequiredArgsConstructor
public class RecipeRegistController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistController.class);
	
	private final RedisService redisService = new RedisService();
	
	private final RecipeRegistService recipeListService;
	
	//메인 레시피 목록 조회
	@RequestMapping(value = "/recipeList/{user_id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String recipeList(@PathVariable String user_id) {
		logger.info("====================== recipeList start ======================");
		//Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
		
		String jsonRecipeList = null;
//		if(redisUser != null) {
			//목록 조회
			List<RecipeRegist> recipeList = recipeListService.getRecipeList(user_id);
			List<RecipeRegist> recipe1List = recipeListService.getRecipeList(user_id); 
			HashMap<String, List> searchMap = new HashMap<String, List>();
			searchMap.put("test", recipeList);
			searchMap.put("test1", recipe1List);
			Gson gson = new Gson();
			jsonRecipeList = gson.toJson(searchMap);
//		}
		logger.info("====================== recipeList end ======================");
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
