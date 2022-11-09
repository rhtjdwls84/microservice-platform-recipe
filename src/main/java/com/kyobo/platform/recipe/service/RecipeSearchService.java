package com.kyobo.platform.recipe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.mapper.RecipeSearchMapper;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeSearchService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeSearchService.class);
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeSearchMapper recipeSearchMapper;
	
	// 레시피 검색 조회
	public List<Map.Entry<String, Object>> searchRecipe(String recipe_search_text, String category_main_name,
			String category_name, String recipe_babyfood_step, String except_material_yn) {
		logger.info("====================== searchRecipe service start ======================");
		
		Map<String, Object> recipe_map = new HashMap<>();
		List<Map.Entry<String, Object>> recipe_list = recipe_map.entrySet().stream().collect(Collectors.toList());
		
		Recipe recipe = new Recipe();
		
		recipe.setRecipe_search_text(recipe_search_text);
		recipe.setCategory_main_name(category_main_name);
		recipe.setCategory_name(category_name);
		recipe.setRecipe_babyfood_step(recipe_babyfood_step);
		recipe.setExcept_material_yn(except_material_yn);
		
		recipe_list = recipeSearchMapper.selectListSearchRecipe(recipe);
        
        logger.info("recipe_list : " + recipe_list);
        logger.info("====================== searchRecipe service end ======================");
        return recipe_list;
    }
}
