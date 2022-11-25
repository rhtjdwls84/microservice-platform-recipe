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
public class RecipeOrder {
	
	private String recipe_key;
	
	private String recipe_order_key;
	
    private String recipe_order;
	
    private String recipe_order_desc;
	
    private String recipe_order_img_key_name;
    
    private String recipe_order_img_path;
	
}
