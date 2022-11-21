package com.kyobo.platform.recipe.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.dao.RecipeMaterial;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.RecipeReview;
import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.mapper.RecipeDetailMapper;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeDetailService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeDetailService.class);
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeDetailMapper recipeDetailMapper;
	
	public Recipe recipeDetail(String recipe_key) {
		logger.info("====================== recipeDetail service start ======================");
		
		// 레시피 조회수 업데이트
		int result = recipeDetailMapper.updateRecipeSearchCnt(recipe_key);
		
		if(result > 0) {
			// 레시피 상세 정보
			Recipe recipe = recipeDetailMapper.selectRecipeDetail(recipe_key);
			
			// 레시피 재료 정보
			ArrayList<RecipeMaterial> recipe_material_list = recipeDetailMapper.selectRecipeMaterial(recipe_key);
			recipe.setRecipe_material_list(recipe_material_list);
			
			// 레시피 순서 정보
			ArrayList<RecipeOrder> recipe_order_list = recipeDetailMapper.selectRecipeOrder(recipe_key);
			recipe.setRecipe_order_list(recipe_order_list);
			
			// 레시피 리뷰 정보
			HashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe_key);
			recipe.setRecipe_review_cnt_info(recipe_review_cnt_info);
			
			// 레시피 작성자 정보
			// 레시피 영양소 정보
			// 레시피 자체재료 정보
			
			logger.info("====================== recipeDetail service end ======================");
			return recipe;
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public HashMap<RecipeReview, Object> registRecipeReview(Recipe recipe) {
		logger.info("====================== registRecipeReview service start ======================");
		
		// 레시피 선택한 리뷰 insert
		String[] recipe_select_review = recipe.getRecipe_select_review().split(",");
		int result = 0;
		
		for(int i = 0; i < recipe_select_review.length; i++) {
			recipe.setRecipe_select_review(recipe_select_review[i]);
			result = recipeDetailMapper.insertRecipeReview(recipe);
			result++;
		}
		
		if(result > 0) {
			// 레시피 리뷰수 정보 리턴
			HashMap<RecipeReview, Object> recipe_review_cnt_info = recipeDetailMapper.selectRecipeReview(recipe.getRecipe_key());
			recipe.setRecipe_review_cnt_info(recipe_review_cnt_info);
			
			logger.info("====================== registRecipeReview service end ======================");
			return recipe_review_cnt_info;
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public void registRecipeScrap(Recipe recipe) {
		logger.info("====================== registRecipeScrap service start ======================");
		
		// 스크랩 등록일 경우 insert 및 레시피 스크랩수 + 1
		if(recipe.getRecipe_scrap_yn().equals("Y")) {
			int result = recipeDetailMapper.insertRecipeScrap(recipe);
			if(result > 0) {
				recipeDetailMapper.updateRecipeScrapPlus(recipe.getRecipe_key());
			}
		// 스크랩 취소일 경우 N으로 업데이트 및 레시피 스크랩수 - 1	
		} else if(recipe.getRecipe_scrap_yn().equals("N")) {
			int result = recipeDetailMapper.updateRecipeScrap(recipe);
			if(result > 0) {
				recipeDetailMapper.updateRecipeScrapMinus(recipe.getRecipe_key());
			}
		} else {
			throw new GlobalExceptionHandler();
		}
	}
}
