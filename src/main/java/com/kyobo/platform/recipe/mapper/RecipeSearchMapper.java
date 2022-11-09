package com.kyobo.platform.recipe.mapper;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.kyobo.platform.recipe.dao.Recipe;

@Mapper
@Repository
public interface RecipeSearchMapper {
	
	@Transactional
	List<Map.Entry<String, Object>> selectListSearchRecipe(Recipe recipe);
}
