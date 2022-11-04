package com.kyobo.platform.recipe.recipeDetail;

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
public class RecipeDetail {

    private String recipe_user_id;
	
    @Id
    private String recipe_serno;
	
    private String recipe_name;
	
    private String recipe_desc;
	
    private String recipe_category;
	 
    private String recipe_lead_time;
	
    private int recipe_level;
	
    private String recipe_babyfood_step;
	
}
