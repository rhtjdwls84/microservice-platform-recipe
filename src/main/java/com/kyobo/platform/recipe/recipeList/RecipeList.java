package com.kyobo.platform.recipe.recipeList;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class RecipeList {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String RECIPE_SERNO;
	
	@Column(length = 20)
    private String USER_ID;
	
	@Column(length = 20)
    private String CATEGORY_SERNO;
	
	@Column(length = 50)
    private String CATEGORY_NAME;
	
	@Column(length = 50)
    private String RECIPE_NAME;
	 
	@Column(length = 1000)
    private String RECIPE_DESC;
	
    private int RECIPE_LEAD_TIME;
	
	@Column(length = 200)
    private String RECIPE_MAIN_IMG_PATH;
	
	@Column(length = 20)
    private String RECIPE_USER_ID;
	
//	@Column(length = 50)
//    private String recipeEditorName;
//
//    @Column(length = 200)
//    private String recipeEditorThumnail;

    @Column
    private int RECIPE_SCRAP_COUNT;
}
