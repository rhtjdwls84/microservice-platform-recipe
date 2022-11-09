package com.kyobo.platform.recipe.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyobo.platform.recipe.dao.Recipe;

public interface RecipeRegistRepository extends JpaRepository<Recipe, String> {
	
}
