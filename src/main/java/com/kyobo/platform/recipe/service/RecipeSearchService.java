package com.kyobo.platform.recipe.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
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
	@SuppressWarnings("unchecked")
	public List<Map.Entry<String, Object>> searchRecipe(String recipe_search_text, String category_main_name,
			String category_name, String recipe_babyfood_step, String except_ingredient_yn, String recipe_tag_desc) {
		logger.info("====================== searchRecipe service start ======================");
		
		LinkedHashMap<String, Object> recipe_map = new LinkedHashMap<>();
		List<Map.Entry<String, Object>> recipe_list = recipe_map.entrySet().stream().collect(Collectors.toList());
		
		Recipe recipe = new Recipe();
		
		recipe.setRecipe_search_text(recipe_search_text);
		recipe.setCategory_main_name(category_main_name);
		recipe.setCategory_name(category_name);
		recipe.setRecipe_babyfood_step(recipe_babyfood_step);
		recipe.setExcept_ingredient_yn(except_ingredient_yn);
		recipe.setRecipe_tag_desc(recipe_tag_desc);
		
		// 아이 알러지정보 및 아이 월령값 세팅
		if(except_ingredient_yn != null) {
			if(except_ingredient_yn.equals("Y")) {
				JSONArray json_array = new JSONArray();
				json_array.add("두유");
				recipe.setRecipe_ingredient_babystep("13");
				recipe.setJson_allergy_array(json_array);
			}
		}
		
		if(recipe.getRecipe_tag_desc() == null) {
			recipe_list = recipeSearchMapper.selectListSearchRecipe(recipe);
			// 조회 된 레시피키를 가지고 재료 조회하여 ML 전달하여 제외된 레시피키값 리턴 받아야 함
		} else {
			recipe_list = recipeSearchMapper.selectListSearchRecipeTag(recipe);
		}
		
        logger.info("recipe_list : " + recipe_list);
        logger.info("====================== searchRecipe service end ======================");
        return recipe_list;
    }
}
