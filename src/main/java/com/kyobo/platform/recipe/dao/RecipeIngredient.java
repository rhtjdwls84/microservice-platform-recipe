package com.kyobo.platform.recipe.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class RecipeIngredient {
	
	private String recipe_key;
	
	private String recipe_ingredient_key;
	
	private String recipe_common_ingredient_key;
	
    private String recipe_ingredient_name;
	
    private String recipe_ingredient_category;
    
    private String recipe_ingredient_main_category;
	
    private String recipe_ingredient_countunit;
    
    private String recipe_ingredient_main_countunit;
    
    private String recipe_ingredient_amount;
    
    private String recipe_ingredient_main_yn;
    
    private String recipe_ingredient_allergy_category;
    
    private String recipe_ingredient_babystep;
}
