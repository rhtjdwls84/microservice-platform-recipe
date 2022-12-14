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
public class RecipeReview {
	
	private String recipe_review_key;
	
    private int recipe_review_cnt_1;
    
    private int recipe_review_cnt_2;
    
    private int recipe_review_cnt_3;
    
    private int recipe_review_cnt_4;
}
