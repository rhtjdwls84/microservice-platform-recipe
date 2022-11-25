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
public class RecipeMaterial {
	
	private String recipe_key;
	
	private String recipe_material_key;
	
	private String recipe_common_material_key;
	
    private String recipe_material_name;
	
    private String recipe_material_classify;
    
    private String recipe_material_main_classify;
	
    private String recipe_material_meature;
    
    private String recipe_material_main_meature;
    
    private String recipe_material_amount;
    
    private String recipe_material_main_yn;
    
    private String recipe_material_allergy_yn;
}
