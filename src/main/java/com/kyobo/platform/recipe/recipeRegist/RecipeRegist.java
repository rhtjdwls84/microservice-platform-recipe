package com.kyobo.platform.recipe.recipeRegist;

import java.util.ArrayList;

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
public class RecipeRegist {
	
    private String recipe_user_id;
	
    @Id
    private String recipe_key;
	
    private String recipe_name;
	
    private String recipe_desc;
	
    private String recipe_category;
	 
    private String recipe_lead_time;
	
    private String recipe_level;
	
    private String recipe_babyfood_step;
	
    private String recipe_main_img_name;
	
    private String recipe_main_img_path;

    private String recipe_img_name_1;

    private String recipe_img_path_1;
    
    private String recipe_img_name_2;

    private String recipe_img_path_2;
    
    private String recipe_img_name_3;

    private String recipe_img_path_3;
    
    private String recipe_img_name_4;

    private String recipe_img_path_4;
    
    private String recipe_health_note;

    private int recipe_servings;
    
    private String recipe_temp_step;
    
    private String recipe_check_status;
    
    private String recipe_write_status;
    
    private ArrayList<RecipeMaterial> recipe_material_arr = new ArrayList<RecipeMaterial>();
    
    private ArrayList<RecipeOrder> recipe_order_arr = new ArrayList<RecipeOrder>();
}
