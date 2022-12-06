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
public class RecipeTag {
	
	private String recipe_tag_no;
	
    private String recipe_tag_desc;
    
}
