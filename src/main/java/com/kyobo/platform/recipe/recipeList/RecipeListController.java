package com.kyobo.platform.recipe.recipeList;

import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kyobo.platform.recipe.redis.RedisService;
import com.kyobo.platform.recipe.redis.RedisUser;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipe")
@Controller
@RequiredArgsConstructor
public class RecipeListController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeListController.class);
	
	private final RedisService redisService = new RedisService();
	
	private final RecipeListService recipeListService;
	
	//메인 레시피 목록 조회
	@RequestMapping(value = "/recipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
    public String recipeList(@RequestParam(value = "id", required = false) String id) {
		logger.info("====================== recipeList start ======================");
		//Redis 세션체크
//		Optional<RedisUser> redisUser = redisService.redisGetSession(id);
		
		String jsonRecipeList = null;
//		if(redisUser != null) {
			//목록 조회
			List<RecipeList> recipeList = recipeListService.getRecipeList();
			Gson gson = new Gson();
			jsonRecipeList = gson.toJson(recipeList);
//		}
		logger.info("====================== recipeList end ======================");
        return jsonRecipeList;     
        
    }
	
	//검색 레시피 목록 조회
	@RequestMapping(value = "/searchRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
    public String searchRecipeList(@RequestParam(value = "search_text", required = false) String search_text, 
    		@RequestParam(value = "search_type", required = false) String search_type) {
		logger.info("====================== recipeList start ======================");
		//Redis 세션체크
//			Optional<RedisUser> redisUser = redisService.redisGetSession(id);
		
		String jsonRecipeList = null;
//			if(redisUser != null) {
			//목록 조회
			List<RecipeList> recipeList = recipeListService.getSearchRecipeList(search_text, search_type);
			Gson gson = new Gson();
			jsonRecipeList = gson.toJson(recipeList);
//			}
		logger.info("====================== recipeList end ======================");
        return jsonRecipeList;     
    }
}
