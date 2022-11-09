package com.kyobo.platform.recipe.mapper;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.kyobo.platform.recipe.dao.RecipeMaterial;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.RecipeRegist;

@Mapper
@Repository
public interface RecipeRegistMapper {
	
	@Transactional
	int insertRecipeDefInfo(RecipeRegist recipeRegist);
	
	@Transactional
	int updateRecipeDefInfo(RecipeRegist recipeRegist);
	
	@Transactional
	int updateRecipeAddInfo(RecipeRegist recipeRegist);
	
	@Transactional
	int updateRecipeMaterialInfo(RecipeRegist recipeRegist);
	
	@Transactional
	int deleteRecipeMaterialInfo(String recipe_key);
	
	@Transactional
	int insertRecipeMaterialInfo(RecipeMaterial recipeMaterial);
	
	@Transactional
	int deleteRecipeOrderInfo(String recipe_key);
	
	@Transactional
	int insertRecipeOrderInfo(RecipeOrder recipeOrder);
	
	@Transactional
	int updateRecipeStatus(RecipeRegist recipeRegist);
	
	@Transactional
	String selectCheckRecipeTempSave(String user_id);
	
	@Transactional
	int deleteRecipeTempSave(String recipe_key);
	
	@Transactional
	List<Map.Entry<String, Object>> selectListRecipeBaseMaterial(String search_text);
}
