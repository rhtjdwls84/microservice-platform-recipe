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
public class RecipeWriter {
	
	private String recipe_user_key;
	
	private String recipe_writer_nickname;
	
    private String recipe_writer_grade;
	
    private String recipe_writer_thumnail_name;
    
    private String recipe_writer_thumnail_path;
}
