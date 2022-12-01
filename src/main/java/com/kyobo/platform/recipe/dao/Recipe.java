package com.kyobo.platform.recipe.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class Recipe {
	
    private String recipe_user_id;
	
    private String recipe_key;
	
    private String recipe_name;
	
    private String recipe_desc;
	
    private String recipe_category;
    
    private String recipe_health_develop;
    
    private String recipe_lead_time;
	
    private String recipe_level;
	
    private String recipe_babyfood_step;
    
    private String recipe_health_note;
    
    private int recipe_scrap_cnt;
    
    private int recipe_view_cnt;
    
    private String recipe_scrap_yn;
    
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
    
    private int recipe_servings;
    
    private int recipe_cal;
    
    private String recipe_reject_msg;
    
    private String recipe_temp_step; //기본정보, 부가정보, 재료, 요리순서, 완료
    
    private String recipe_check_status; //임시저장, 검수대기, 검수완료, 보완요청, 등록완료
    
    private String recipe_write_status; //임시저장, 수정
    
    // 레시피 재료 테이블
    private ArrayList<RecipeIngredient> recipe_ingredient_list = new ArrayList<RecipeIngredient>();
    
    // 레시피 순서 테이블
    private ArrayList<RecipeOrder> recipe_order_list = new ArrayList<RecipeOrder>();
    
    private String recipe_search_text;
    
    private String category_main_name;
    
    private String category_name;
    
    private String except_ingredient_yn;
    
    private int recipe_tag_no;
    
    private String recipe_tag_desc;
    
    private String recipe_tag_type;
    
    private String recipe_select_review;
    
    private String created_datetime;
    
    private String last_modified_datetime;
    
    // 레시피 리뷰 카운트
    private HashMap<RecipeReview, Object> recipe_review_cnt_info = new HashMap<RecipeReview, Object>();
    
    // 레시피 작성자 정보
    private HashMap<RecipeWriter, Object> recipe_writer_info = new HashMap<RecipeWriter, Object>();
    
    //레시피 섭취자제재료 정보(ML 연계)
    private ArrayList<Map<String, Object>> recipe_control_ingredient_list = new ArrayList<Map<String, Object>>();
    
    //레시피 알러지재료 정보(ML 연계)
    private ArrayList<Map<String, Object>> recipe_allergy_ingredient_list = new ArrayList<Map<String, Object>>();
    
    //레시피 영양소 정보(ML 연계)
    private ArrayList<Map<String, Object>> recipe_nutrient_ingredient_list = new ArrayList<Map<String, Object>>();
    
    //레시피 태그 정보
    private List<Map.Entry<String, Object>> recipe_tag_list = new ArrayList<Map.Entry<String,Object>>();
}

