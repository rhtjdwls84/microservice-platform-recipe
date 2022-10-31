package com.kyobo.platform.recipe.recipeDetail;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RecipeDetailMapper {
	List<RecipeDetail> getRecipeList(String user_id);
	List<RecipeDetail> getSearchRecipeList(HashMap<String, String> map);
}
