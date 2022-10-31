package com.kyobo.platform.recipe.recipeRegist;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kyobo.platform.recipe.config.GlobalExceptionHandler;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeRegistService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistService.class);
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeRegistMapper recipeListMapper;
	
	@Cacheable(value = "RecipeList", key = "#recipe_user_id", cacheManager = "cacheManager")
	public List<RecipeRegist> getRecipeList(String recipe_user_id) {
		logger.info("====================== getRecipeList start ======================");
//		Optional<RecipeList> recipeList = recipeListRepository.findById(user_id);
		List<RecipeRegist> recipeList = recipeListMapper.getRecipeList(recipe_user_id);
		if(recipeList != null) {
			logger.info("====================== getRecipeList end ======================");
			return recipeList;
		}else {
			throw new GlobalExceptionHandler();
		}
	}
	
//	public List<RecipeList> getSearchRecipeList(String search_text, String search_type) {
//		logger.info("====================== getSearchRecipeList start ======================");
//		List<RecipeList> recipeList = this.recipeListRepository.getSearchRecipeList(search_text, search_type);
//		HashMap<String, String> searchMap = new HashMap<String, String>();
//		searchMap.put("search_text", search_text);
//		searchMap.put("search_type", search_type);
//		List<RecipeList> recipeList = recipeListMapper.getSearchRecipeList(searchMap);
//		if(recipeList != null) {
//			logger.info("====================== getSearchRecipeList end ======================");
//			return recipeList;
//		}else {
//			throw new GlobalExceptionHandler();
//		}
//	}
	
}
