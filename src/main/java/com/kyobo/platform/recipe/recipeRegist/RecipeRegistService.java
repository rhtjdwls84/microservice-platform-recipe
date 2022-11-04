package com.kyobo.platform.recipe.recipeRegist;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jets3t.service.CloudFrontServiceException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.config.AWSConfig;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeRegistService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistService.class);
	
//	private final RecipeListRepository recipeListRepository;
	
	private final RecipeRegistMapper recipeRegistMapper;
	
	private final AmazonS3 amazonS3;
	
	AWSConfig awsConfig = new AWSConfig();
	
	public String insertRecipe(RecipeRegist recipeRegist) {
		logger.info("====================== insertRecipe start ======================");
		
		String recipe_write_status = recipeRegist.getRecipe_write_status();
		if(recipe_write_status.equals("임시저장")) {
			recipeRegist.setRecipe_check_status("임시저장중");
		}
		recipeRegistMapper.insertRecipe(recipeRegist);
		
		ArrayList<RecipeMaterial> recipeMaterial = recipeRegist.getRecipe_material_arr();
		
		for(int i = 0; i < recipeMaterial.size(); i++) {
			recipeMaterial.get(i).setRecipe_key(recipeRegist.getRecipe_key());
			recipeRegistMapper.insertRecipeMaterial(recipeMaterial.get(i));
		}
		
		ArrayList<RecipeOrder> recipeOrder = recipeRegist.getRecipe_order_arr();
		
		for(int i = 0; i < recipeOrder.size(); i++) {
			recipeOrder.get(i).setRecipe_key(recipeRegist.getRecipe_key());
			recipeRegistMapper.insertRecipeOrder(recipeOrder.get(i));
		}
		
		if(recipeRegist.getRecipe_key() != null) {
			logger.info("====================== insertRecipe end ======================");
			return recipeRegist.getRecipe_key();
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	public List<String> imageUpload(List<MultipartFile> multipartFiles) throws IOException, ParseException, CloudFrontServiceException {
		logger.info("====================== imageUpload start ======================");
		String signedUrl = "";
		List<String> signedUrls = new ArrayList<String>();
		
		// 다중 파일 처리
        for(MultipartFile multipartFile : multipartFiles) {
        	String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            //s3 업로드
            amazonS3.putObject("kyobo-backend-bucket", s3FileName, multipartFile.getInputStream(), objMeta);
            
            //cloudfront signed URL 생성
            awsConfig.CloudFrontManager();
            signedUrl = awsConfig.createSignedUrlCanned(s3FileName);
            logger.info("signedUrl : " + signedUrl);
            
            signedUrls.add(signedUrl);
        }
        
        logger.info("signedUrls : " + signedUrls);
        logger.info("====================== imageUpload end ======================");
        return signedUrls;
    }
	
//	@Cacheable(value = "RecipeList", key = "#recipe_user_id", cacheManager = "cacheManager")
//	public List<RecipeList> getSearchRecipeList(String search_text, String search_type) {
//		logger.info("====================== getSearchRecipeList start ======================");
//		List<RecipeList> recipeList = this.recipeListRepository.getSearchRecipeList(search_text, search_type);
//		HashMap<String, String> searchMap = new HashMap<String, String>();
//		searchMap.put("search_text", search_text);
//		searchMap.put("search_type", search_type);
//		List<RecipeList> recipeList = recipeListMapper.getSearchRecipeList(searchMap);
//		if(recipeList != null) {
//			logger.info("====================== getSearchRecipeList end ======================");
//			return recipeList;
//		}else {
//			throw new GlobalExceptionHandler();
//		}
//	}
	
}
