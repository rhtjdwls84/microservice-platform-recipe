package com.kyobo.platform.recipe.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobo.platform.recipe.dao.Recipe;

@Mapper
@Repository
public interface RecipeSearchMapper {
	
	@Transactional
	List<Map.Entry<String, Object>> selectListSearchRecipe(Recipe recipe);
	
	@Transactional
	List<Map.Entry<String, Object>> selectListSearchRecipeTag(Recipe recipe);
	
	@Transactional
	List<Map.Entry<String, Object>> selectListUserWriteRecipe(String user_key);
	
	@Transactional
	List<Map.Entry<String, Object>> selectListUserScrapRecipe(String user_key);
	
	@Transactional
	LinkedHashMap<String, Object> selectUserRecipe(String user_key);
}
