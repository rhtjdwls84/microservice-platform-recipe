package com.kyobo.platform.recipe.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RecipeMainMapper {
	@Transactional
	List<Map<String, Object>> selectRecipeList(HashMap<String, Object> interest_map);
	
	@Transactional
	List<Map<String, Object>> seasonMaterialBasedRecipeList(String season_material);
	
	@Transactional
	int selectRecipeReviewCount(String recipe_key);
}
