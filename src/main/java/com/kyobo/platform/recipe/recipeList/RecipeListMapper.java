package com.kyobo.platform.recipe.recipeList;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RecipeListMapper {
	List<RecipeList> getRecipeList(String user_id);
	List<RecipeList> getSearchRecipeList(HashMap<String, String> map);
}
