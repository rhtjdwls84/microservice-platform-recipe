package com.kyobo.platform.recipe.recipeDetail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kyobo.platform.recipe.recipeList.RecipeList;

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
@Table(name = "RECIPE_TB")
public class RecipeDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String RECIPE_SERNO;
	
//    private String USER_ID;
	
    private String CATEGORY_SERNO;
	
//    private String CATEGORY_NAME;
	
    private String RECIPE_NAME;
	 
    private String RECIPE_DESC;
	
    private int RECIPE_LEAD_TIME;
	
    private String RECIPE_MAIN_IMG_PATH;
	
    private String RECIPE_USER_ID;
	
//    private String RECIPE_USER_NAME;
//
//    private String RECIPE_USER_THUMNAIL;

    private int RECIPE_SCRAP_COUNT;
}
