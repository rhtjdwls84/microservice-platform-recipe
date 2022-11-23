package com.kyobo.platform.recipe.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.jets3t.service.CloudFrontServiceException;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.google.gson.JsonObject;
//import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.config.HttpConfig;
import com.kyobo.platform.recipe.dao.RecipeMaterial;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.mapper.RecipeRegistMapper;
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
	
	public String recipeDefInfo(Recipe recipe) {
		logger.info("====================== recipeDefInfo service start ======================");
		
		String recipe_write_status = recipe.getRecipe_write_status();
		recipe.setRecipe_check_status("임시저장중");
		recipe.setRecipe_temp_step("기본정보");
		
		if(recipe_write_status.equals("임시저장")) {
			recipeRegistMapper.insertRecipeDefInfo(recipe);
		} else {
			recipeRegistMapper.updateRecipeDefInfo(recipe);
		}
		
		if(recipe.getRecipe_key() != null) {
			logger.info("====================== recipeDefInfo service end ======================");
			return recipe.getRecipe_key();
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	// 레시피 부가정보 작성
	public String recipeAddInfo(Recipe recipe) {
		logger.info("====================== recipeAddInfo service start ======================");
		
		recipe.setRecipe_check_status("임시저장중");
		recipe.setRecipe_temp_step("부가정보");
		recipeRegistMapper.updateRecipeAddInfo(recipe);
		
		if(recipe.getRecipe_key() != null) {
			logger.info("====================== recipeAddInfo service end ======================");
			return recipe.getRecipe_key();
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	// 레시피 재료정보 작성
	public String recipeMaterialInfo(Recipe recipe) {
		logger.info("====================== recipeMaterialInfo service start ======================");
		ArrayList<RecipeMaterial> recipe_material_list = recipe.getRecipe_material_list();
		
		// 기존 레시피 재료정보는 삭제
		int result = recipeRegistMapper.deleteRecipeMaterialInfo(recipe.getRecipe_key());
		
		if(result >= 0) {
			for(int i = 0; i < recipe_material_list.size(); i++) {
				recipe_material_list.get(i).setRecipe_key(recipe.getRecipe_key());
				result = recipeRegistMapper.insertRecipeMaterialInfo(recipe_material_list.get(i));
				
				if(recipe_material_list.get(i).getRecipe_material_main_yn().equals("Y")) {
					recipe.setRecipe_tag_desc(recipe_material_list.get(i).getRecipe_material_name());
					recipeRegistMapper.insertRecipeTag(recipe);
					
				}
			}
			if(result > 0) {
				// 레시피 몇 인분 수 업데이트
				recipe.setRecipe_check_status("임시저장중");
				recipe.setRecipe_temp_step("재료");
				recipeRegistMapper.updateRecipeMaterialInfo(recipe);
			}
		}
		
		if(recipe.getRecipe_key() != null) {
			logger.info("====================== recipeMaterialInfo service end ======================");
			return recipe.getRecipe_key();
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	// 레시피 순서정보 작성
	public String recipeOrderInfo(Recipe recipe) {
		logger.info("====================== recipeOrderInfo service start ======================");
		ArrayList<RecipeOrder> recipe_order_list = recipe.getRecipe_order_list();
		
		// 기존 레시피 순서정보는 삭제
		int result = recipeRegistMapper.deleteRecipeOrderInfo(recipe.getRecipe_key());
		
		if(result >= 0) {
			for(int i = 0; i < recipe_order_list.size(); i++) {
				recipe_order_list.get(i).setRecipe_key(recipe.getRecipe_key());
				result = recipeRegistMapper.insertRecipeOrderInfo(recipe_order_list.get(i));
			}
			// 레시피 현재 임시저장 상태 업데이트
			if(result > 0) {
				recipe.setRecipe_check_status("임시저장중");
				recipe.setRecipe_temp_step("요리순서");
				recipeRegistMapper.updateRecipeStatus(recipe);
			}
		}
		
		if(recipe.getRecipe_key() != null) {
			logger.info("====================== recipeOrderInfo service end ======================");
			return recipe.getRecipe_key();
		} else {
			throw new GlobalExceptionHandler();
		}
	}
	
	// 레시피 이미지 업로드
	public List<Map<String, Object>> recipeImageUpload(List<MultipartFile> multipartFiles)
			throws IOException, ParseException, CloudFrontServiceException {
		logger.info("====================== recipeImageUpload service start ======================");
		
		String recipe_image_signed_url = "";
		List<Map<String, Object>> recipe_image_url_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> recipe_image_url_map = new HashMap<String, Object>();
		
		// 다중 파일 처리
        for(MultipartFile multipartFile : multipartFiles) {
        	// s3에 저장될 경로가 정해지면 keyname에 경로 추가 필요함
        	String recipe_image_key_name = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            
//            ObjectMetadata objMeta = new ObjectMetadata();
//            objMeta.setContentLength(multipartFile.getInputStream().available());
            
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            
            //이미지 압축
            File comp_image_file = new File(recipe_image_key_name);
            OutputStream os = new FileOutputStream(comp_image_file);

            // 이미지 확장자의 경우 정해지는건지 아니면 파일의 확장명을 분리해서 세팅
            String splitData[] = multipartFile.getOriginalFilename().split("\\.");
            String file_ext = splitData[(splitData.length)-1];
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(file_ext);
            ImageWriter writer = (ImageWriter) writers.next();
         
            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);  // Change the quality value you prefer
            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();

            // s3 업로드
//            amazonS3.putObject("kyobo-backend-bucket", s3FileName, multipartFile.getInputStream(), objMeta);
            amazonS3.putObject("kyobo-backend-bucket", recipe_image_key_name, comp_image_file);
            
            // cloudfront signed URL 생성
            awsConfig.CloudFrontManager();
            recipe_image_signed_url = awsConfig.createSignedUrlCanned(recipe_image_key_name);
            
            recipe_image_url_map.put("recipe_image_signed_url", recipe_image_signed_url);
            recipe_image_url_map.put("recipe_image_key_name", recipe_image_key_name);
            
            logger.info("recipe_image_signed_url : " + recipe_image_signed_url);
            logger.info("recipe_image_key_name : " + recipe_image_key_name);
            
            recipe_image_url_list.add(recipe_image_url_map);
        }
        
        logger.info("recipe_image_url_list : " + recipe_image_url_list);
        logger.info("====================== recipeImageUpload service end ======================");
        return recipe_image_url_list;
    }
	
	// 레시피 임시저장 체크
	public String CheckRecipeTempSave(String user_id) {
		logger.info("====================== CheckRecipeTempSave service start ======================");
		
		String recipe_key = recipeRegistMapper.selectCheckRecipeTempSave(user_id);
        
        logger.info("recipe_key : " + recipe_key);
        logger.info("====================== CheckRecipeTempSave service end ======================");
        return recipe_key;
    }
	
	// 레시피 임시저장 삭제
	public int deleteRecipeTempSave(String user_id) {
		logger.info("====================== deleteRecipeTempSave service start ======================");
		
		// 로그인 사용자의 임시저장된 레시피가 있는지 조회하여 recipe_key return
		String recipe_key = recipeRegistMapper.selectCheckRecipeTempSave(user_id);
		int result = 0;
		
		result = recipeRegistMapper.deleteRecipeMaterialInfo(recipe_key);
		
		if(result >= 0) {
			result = recipeRegistMapper.deleteRecipeOrderInfo(recipe_key);
			if(result >= 0) {
				result = recipeRegistMapper.deleteRecipeTempSave(recipe_key);
			}
		}
        
        logger.info("====================== deleteRecipeTempSave service end ======================");
        return result;
    }
	
	// 레시피 베이스 재료 조회
	public List<Map.Entry<String, Object>> listRecipeBaseMaterial(String search_text) {
		logger.info("====================== listRecipeBaseMaterial service start ======================");
		
		Map<String, Object> recipe_base_material_map = new HashMap<>();
		List<Map.Entry<String, Object>> recipe_base_material_list = 
				recipe_base_material_map.entrySet().stream().collect(Collectors.toList());
		
		recipe_base_material_list = recipeRegistMapper.selectListRecipeBaseMaterial(search_text);
        
        logger.info("recipe_base_material_list : " + recipe_base_material_list);
        logger.info("====================== listRecipeBaseMaterial service end ======================");
        return recipe_base_material_list;
    }
	
	// 레시피 업로드
	public int recipeUpload(String recipe_key) {
		logger.info("====================== recipeUpload service start ======================");
		
		JSONObject responseJson = null;
		
		Recipe recipe = new Recipe();
		recipe.setRecipe_check_status("검수대기");
		recipe.setRecipe_temp_step("완료");
		recipe.setRecipe_key(recipe_key);
		int result = recipeRegistMapper.updateRecipeStatus(recipe);
		
		// 건강태그, 칼로리 업데이트(ML 영역 호출)
		if(result > 0) {
			HttpConfig httpConfig = new HttpConfig();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("recipe_id", recipe_key);
			String url = "/recipe/analysis";
			String type = "POST";
			
			responseJson = httpConfig.callApi(jsonObject, url, type);
			
			recipe.setRecipe_health_tag(responseJson.get("health_tags").toString());
			recipe.setRecipe_cal((int) responseJson.get("calory"));
			
			result = recipeRegistMapper.updateRecipeAnalysis(recipe);
		}
		
		logger.info("====================== recipeUpload service end ======================");
		return result;
	}
	
	public void recipeImageDelete(String imageFileName) {
		logger.info("====================== recipeImageDelete service start ======================");
		
//		awsConfig.s3ImageDelete(imageFileName);
		try {
			amazonS3.deleteObject("kyobo-backend-bucket", imageFileName);
		} catch(Exception e) {
			e.printStackTrace();
		}
        
        logger.info("====================== recipeImageDelete service end ======================");
    }
}
