package com.kyobo.platform.recipe.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobo.platform.recipe.dao.RecipeMaterial;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.Recipe;

@Mapper
@Repository
public interface RecipeRegistMapper {
	
	@Transactional
	int insertRecipeDefInfo(Recipe recipe);
	
	@Transactional
	int updateRecipeDefInfo(Recipe recipe);
	
	@Transactional
	int updateRecipeAddInfo(Recipe recipe);
	
	@Transactional
	int updateRecipeMaterialInfo(Recipe recipe);
	
	@Transactional
	int deleteRecipeMaterialInfo(String recipe_key);
	
	@Transactional
	int insertRecipeMaterialInfo(RecipeMaterial recipeMaterial);
	
	@Transactional
	int deleteRecipeOrderInfo(String recipe_key);
	
	@Transactional
	int insertRecipeOrderInfo(RecipeOrder recipeOrder);
	
	@Transactional
	int updateRecipeStatus(Recipe recipe);
	
	@Transactional
	String selectCheckRecipeTempSave(String user_id);
	
	@Transactional
	int deleteRecipeTempSave(String recipe_key);
	
	@Transactional
	int insertRecipeTag(Recipe recipe);
	
	@Transactional
	List<Map.Entry<String, Object>> selectListRecipeBaseMaterial(String search_text);
	
	@Transactional
	int updateRecipeAnalysis(Recipe recipe);
	
	@Transactional
	int updateRecipeImage(Map<String, Object> recipe_image_url_map);
	
	@Transactional
	int updateRecipeOrderImage(Map<String, Object> recipe_image_url_map);
}
