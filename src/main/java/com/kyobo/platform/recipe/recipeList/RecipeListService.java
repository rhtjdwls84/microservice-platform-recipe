package com.kyobo.platform.recipe.recipeList;

import java.util.HashMap;
import java.util.List;

import org.hibernate.mapping.Map;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kyobo.platform.recipe.config.GlobalExceptionHandler;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeListService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeListController.class);
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeListMapper recipeListMapper;
	
	public List<RecipeList> getRecipeList() {
		logger.info("====================== getRecipeList start ======================");
		//List<RecipeList> recipeList = this.recipeListRepository.getRecipeList();
		List<RecipeList> recipeList = recipeListMapper.getRecipeList();
		if(recipeList != null) {
			logger.info("====================== getRecipeList end ======================");
			return recipeList;
		}else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<RecipeList> getSearchRecipeList(String search_text, String search_type) {
		logger.info("====================== getSearchRecipeList start ======================");
//		List<RecipeList> recipeList = this.recipeListRepository.getSearchRecipeList(search_text, search_type);
		HashMap<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("search_text", search_text);
		searchMap.put("search_type", search_type);
		List<RecipeList> recipeList = recipeListMapper.getSearchRecipeList(searchMap);
		if(recipeList != null) {
			logger.info("====================== getSearchRecipeList end ======================");
			return recipeList;
		}else {
			throw new GlobalExceptionHandler();
		}
	}
	
}
