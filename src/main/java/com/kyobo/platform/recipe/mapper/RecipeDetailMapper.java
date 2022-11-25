package com.kyobo.platform.recipe.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobo.platform.recipe.dao.RecipeMaterial;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.RecipeReview;
import com.kyobo.platform.recipe.dao.Recipe;

@Mapper
@Repository
public interface RecipeDetailMapper {
	
	@Transactional
	int updateRecipeSearchCnt(String recipe_key);
	
	@Transactional
	Recipe selectRecipeDetail(String recipe_key);
	
	@Transactional
	ArrayList<RecipeMaterial> selectRecipeMaterial(String recipe_key);
	
	@Transactional
	ArrayList<RecipeOrder> selectRecipeOrder(String recipe_key);
	
	@Transactional
	HashMap<RecipeReview, Object> selectRecipeReview(String recipe_key);
	
	@Transactional
	int insertRecipeReview(Recipe recipe);
	
	@Transactional
	int insertRecipeScrap(Recipe recipe);
	
	@Transactional
	int updateRecipeScrap(Recipe recipe);
	
	@Transactional
	int updateRecipeScrapPlus(String recipe_key);
	
	@Transactional
	int updateRecipeScrapMinus(String recipe_key);
}
