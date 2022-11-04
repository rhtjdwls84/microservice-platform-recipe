package com.kyobo.platform.recipe.recipeRegist;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RecipeRegistMapper {
	
	@Transactional
	void insertRecipeDefaultStep(RecipeRegist recipeRegist);
	
	@Transactional
	void updateRecipeDefaultStep(RecipeRegist recipeRegist);
	
	@Transactional
	void updateRecipeSideInfoStep(RecipeRegist recipeRegist);
	
	@Transactional
	void updateRecipeMaterialStep(RecipeRegist recipeRegist);
	
	@Transactional
	void deleteRecipeMaterial(String recipe_key);
	
	@Transactional
	void insertRecipeMaterial(RecipeMaterial recipeMaterial);
	
	@Transactional
	void deleteRecipeOrder(String recipe_key);
	
	@Transactional
	void insertRecipeOrder(RecipeOrder recipeOrder);
	
	@Transactional
	void updateRecipeStatus(RecipeRegist recipeRegist);
	
	List<RecipeRegist> getSearchRecipeList(HashMap<String, String> map);
}
