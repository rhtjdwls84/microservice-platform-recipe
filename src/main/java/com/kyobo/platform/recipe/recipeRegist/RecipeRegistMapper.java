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
	void insertRecipe(RecipeRegist recipeRegist);
	
	@Transactional
	void insertRecipeMaterial(RecipeMaterial recipeMaterial);
	
	@Transactional
	void insertRecipeOrder(RecipeOrder recipeOrder);
	
	List<RecipeRegist> getSearchRecipeList(HashMap<String, String> map);
}
