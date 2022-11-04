package com.kyobo.platform.recipe.recipeRegist;

import javax.persistence.Entity;
import javax.persistence.Id;

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
@Entity
public class RecipeMaterial {
	
	private String recipe_key;
	
	@Id
	private String recipe_material_key;
	
    private String recipe_material_name;
	
    private String recipe_material_classify;
    
    private String recipe_material_main_classify;
	
    private String recipe_material_meature;
	
}