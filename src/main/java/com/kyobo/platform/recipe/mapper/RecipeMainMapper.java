package com.kyobo.platform.recipe.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Repository
public interface RecipeMainMapper {
	@Transactional
	List<Map<String, Object>> selectRecipeList(HashMap<String, Object> interest_map);
	
	@Transactional
	List<Map<String, Object>> seasonIngredientBasedRecipeList(LinkedHashMap<String, Object> map);
	
	@Transactional
	int selectRecipeReviewCount(String recipe_key);
}
