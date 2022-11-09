package com.kyobo.platform.recipe.dao;

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
public class Recipe {
	
    private String recipe_user_id;
	
    @Id
    private String recipe_key;
	
    private String recipe_name;
	
    private String recipe_desc;
	
    private String recipe_category;
    
    private String recipe_health_develop;
	 
    private String recipe_lead_time;
	
    private String recipe_level;
	
    private String recipe_babyfood_step;
	
    private String recipe_main_img_key_name;
	
    private String recipe_main_img_path;

    private String recipe_img_key_name_1;

    private String recipe_img_path_1;
    
    private String recipe_img_key_name_2;

    private String recipe_img_path_2;
    
    private String recipe_img_key_name_3;

    private String recipe_img_path_3;
    
    private String recipe_img_key_name_4;

    private String recipe_img_path_4;
    
    private String recipe_health_note;

    private int recipe_servings;
    
    private String recipe_temp_step; //기본정보, 부가정보, 재료, 요리순서, 완료
    
    private String recipe_check_status; //임시저장, 검수대기, 검수완료, 보완요청, 등록완료
    
    private String recipe_write_status; //임시저장, 수정
    
    private ArrayList<RecipeMaterial> recipe_material_arr = new ArrayList<RecipeMaterial>();
    
    private ArrayList<RecipeOrder> recipe_order_arr = new ArrayList<RecipeOrder>();
    
    private String recipe_search_text;
    
    private String category_main_name;
    
    private String category_name;
    
    private String except_material_yn;
    
    //재료
    private String recipe_material_name;
    
    private String recipe_material_classify;
    
    private String recipe_material_main_classify;
    
    private String recipe_material_meature;
}

