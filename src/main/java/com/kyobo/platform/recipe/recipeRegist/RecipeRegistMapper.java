package com.kyobo.platform.recipe.recipeRegist;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RecipeRegistMapper {
	List<RecipeRegist> getRecipeList(String user_id);
	List<RecipeRegist> getSearchRecipeList(HashMap<String, String> map);
}
