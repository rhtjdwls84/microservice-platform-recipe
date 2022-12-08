package com.kyobo.platform.recipe.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.jets3t.service.CloudFrontServiceException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.config.HttpConfig;
import com.kyobo.platform.recipe.dao.RecipeIngredient;
import com.kyobo.platform.recipe.dao.RecipeOrder;
import com.kyobo.platform.recipe.dao.Recipe;
import com.kyobo.platform.recipe.mapper.RecipeDetailMapper;
import com.kyobo.platform.recipe.mapper.RecipeRegistMapper;
import com.kyobo.platform.recipe.config.AWSConfig;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeRegistService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RecipeRegistService.class);
	
	private final RecipeRegistMapper recipeRegistMapper;
	
	private final RecipeDetailMapper recipeDetailMapper;
	
	@Value("${cloud.aws.region.static}")
    private String region;
	
	AWSConfig aws_config = new AWSConfig();
	
	/* 사전작업
     * SecretAccess pem key를 아래 명령어로 DER 파일로 변환시킨 후 privateKeyFilePath 경로에 추가한다.
     * openssl genrsa -out pk8-APKAZ3MKOJFETMDPAWV6.pem 2048
     * openssl rsa -pubout -in pk8-APKAZ3MKOJFETMDPAWV6.pem -out rsa-APKAZ3MKOJFETMDPAWV6.pem
     * openssl pkcs8 -topk8 -nocrypt -in pk8-APKAZ3MKOJFETMDPAWV6.pem -inform PEM -out pk8-APKAZ3MKOJFETMDPAWV6.der -outform DER
     */
    private String properties_url = "https://kyobo-common-bucket.s3.ap-northeast-2.amazonaws.com/awsAuth.properties";
    
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
	public String recipeIngredientInfo(Recipe recipe) {
		logger.info("====================== recipeIngredientInfo service start ======================");
		ArrayList<RecipeIngredient> recipe_ingredient_list = recipe.getRecipe_ingredient_list();
		
		// 기존 레시피 재료정보는 삭제
		int result = recipeRegistMapper.deleteRecipeIngredientInfo(recipe.getRecipe_key());
		
		if(result >= 0) {
			for(int i = 0; i < recipe_ingredient_list.size(); i++) {
				recipe_ingredient_list.get(i).setRecipe_key(recipe.getRecipe_key());
				result = recipeRegistMapper.insertRecipeIngredientInfo(recipe_ingredient_list.get(i));
				
				if(recipe_ingredient_list.get(i).getRecipe_ingredient_main_yn().equals("Y")) {
					recipe.setRecipe_tag_no(i + 1);
					recipe.setRecipe_tag_desc(recipe_ingredient_list.get(i).getRecipe_ingredient_name());
					recipe.setRecipe_tag_type("사용자태그");
					recipeRegistMapper.insertRecipeTag(recipe);
					
				}
			}
			if(result > 0) {
				// 레시피 몇 인분 수 업데이트
				recipe.setRecipe_check_status("임시저장중");
				recipe.setRecipe_temp_step("재료");
				recipeRegistMapper.updateRecipeIngredientInfo(recipe);
			}
		}
		
		if(recipe.getRecipe_key() != null) {
			logger.info("====================== recipeIngredientInfo service end ======================");
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
	public List<Map<String, Object>> recipeImageUpload(List<MultipartFile> multipartFiles, String recipe_key, String recipe_image_type)
			throws IOException, AmazonServiceException, SdkClientException, CloudFrontServiceException, ParseException, URISyntaxException {
		logger.info("====================== recipeImageUpload service start ======================");
		
		List<Map<String, Object>> recipe_image_url_list = new ArrayList<Map<String, Object>>();
		LinkedHashMap<String, Object> recipe_image_url_map = new LinkedHashMap<String, Object>();
		
		int index = 0;
		String recipe_image_key_name = "";
		String recipe_image_prefix = "";
		
    	//cloudfront 사용을 위한 인증 properties 파일 로드
    	Properties properties = new Properties();
    	URL url = new URL(properties_url);
        properties.load(url.openStream());
        
        String recipe_image_bucket = properties.getProperty("recipeImageBucket");
        String distributionDomain = properties.getProperty("distributionDomain");
        
        ObjectMetadata objMeta = new ObjectMetadata();
        
    	// 현재 날짜 구하기
    	LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 포맷 적용
        String formatedNow = now.format(formatter);
        
        aws_config.CloudFrontManager();
		
		// 다중 파일 처리
        for(MultipartFile multipartFile : multipartFiles) {
        	// 이미지 확장자의 경우 정해지는건지 아니면 파일의 확장명을 분리해서 세팅
            String splitData[] = multipartFile.getOriginalFilename().split("\\.");
            String file_ext = splitData[(splitData.length)-1];
            
        	// s3에 저장될 경로가 정해지면 keyname에 경로 추가 필요함
            if(recipe_image_type.equals("recipe") && index == 0) {
            	recipe_image_prefix = "recipe/" + recipe_key + "/main/";
            	recipe_image_key_name = recipe_key + "-main-" + formatedNow + "-" + multipartFile.getOriginalFilename();
            } else if(recipe_image_type.equals("recipe") && index > 0) {
            	recipe_image_type = "sub";
            	recipe_image_prefix = "recipe/" + recipe_key + "/sub/";
            	recipe_image_key_name = recipe_key + "-sub-" + index + "-" + 
            		formatedNow + "-" + multipartFile.getOriginalFilename();
            } else {
            	recipe_image_key_name = recipe_key + "-" + recipe_image_type + "-" + index + "-" + 
            		formatedNow + "-" + multipartFile.getOriginalFilename();
            }
            
            objMeta.setContentLength(multipartFile.getInputStream().available());
            
            BufferedImage recipe_image_buf = ImageIO.read(multipartFile.getInputStream());
            
            //이미지 압축
            File recipe_image_file = new File(recipe_image_key_name);
            OutputStream os = new FileOutputStream(recipe_image_file);
            
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(file_ext);
            ImageWriter writer = (ImageWriter) writers.next();
         
            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);
          
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//            param.setCompressionQuality(1f); // Change the quality value you prefer
//            writer.write(null, new IIOImage(image, null, null), param);
            writer.write(new IIOImage(recipe_image_buf, null, null));
          
            os.close();
            ios.close();
            writer.dispose();

            // s3 업로드
            AmazonS3 amazonS3 = aws_config.amazonS3(region);
            
            amazonS3.putObject(new PutObjectRequest(recipe_image_bucket, recipe_image_prefix + recipe_image_key_name, recipe_image_file));
            
            recipe_image_file.delete();
            
            if(recipe_image_type.equals("recipe")) {
            	recipe_image_url_map.put("recipe_image_signed_url_" + index, distributionDomain + recipe_image_prefix + recipe_image_key_name);
                recipe_image_url_map.put("recipe_image_key_name_" + index, recipe_image_key_name);
            } else if(recipe_image_type.equals("order")) {
            	recipe_image_url_map.put("recipe_image_signed_url", distributionDomain + recipe_image_prefix + recipe_image_key_name);
                recipe_image_url_map.put("recipe_image_key_name", recipe_image_key_name);
                recipe_image_url_map.put("index", index + 1);
            }
            recipe_image_url_map.put("recipe_key", recipe_key);
            
            
            logger.info("recipe_image_signed_url : " + distributionDomain + recipe_image_prefix + recipe_image_key_name);
            logger.info("recipe_image_key_name : " + recipe_image_key_name);
            
            recipe_image_url_list.add(recipe_image_url_map);
            index++;
        }
        
        if(recipe_image_type.equals("recipe")) {
        	recipeRegistMapper.updateRecipeImage(recipe_image_url_map);
        } else if(recipe_image_type.equals("order")) {
        	for(int i = 0; i < recipe_image_url_list.size(); i++) {
    			recipeRegistMapper.updateRecipeOrderImage(recipe_image_url_list.get(i));
    		}
        }
        
        logger.info("recipe_image_url_list : " + recipe_image_url_list);
        logger.info("====================== recipeImageUpload service end ======================");
		return recipe_image_url_list;
    }
	
	// 레시피 임시저장 체크
	public String CheckRecipeTempSave(String user_key) {
		logger.info("====================== CheckRecipeTempSave service start ======================");
		
		String recipe_key = recipeRegistMapper.selectCheckRecipeTempSave(user_key);
        
        logger.info("recipe_key : " + recipe_key);
        logger.info("====================== CheckRecipeTempSave service end ======================");
        return recipe_key;
    }
	
	// 레시피 임시저장 삭제
	public int deleteRecipeTempSave(String user_key) {
		logger.info("====================== deleteRecipeTempSave service start ======================");
		
		// 로그인 사용자의 임시저장된 레시피가 있는지 조회하여 recipe_key return
		String recipe_key = recipeRegistMapper.selectCheckRecipeTempSave(user_key);
		int result = 0;
		
		result = recipeRegistMapper.deleteRecipeIngredientInfo(recipe_key);
		
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
	@SuppressWarnings("unchecked")
	public JSONObject listRecipeBaseIngredient(String recipe_ingredient_category) throws ParseException, FileNotFoundException, IOException {
		logger.info("====================== listRecipeBaseIngredient service start ======================");
		
		// 베이스 재료 조회(ML 영역 호출)
		HttpConfig httpConfig = new HttpConfig();
		JSONObject json_object = new JSONObject();
		JSONObject response_json = null;
		json_object.put("recipe_ingredient_category", recipe_ingredient_category);
		
		Properties properties = new Properties();
		URL property_url = new URL(properties_url);
        properties.load(property_url.openStream());
        
        String ml_url = properties.getProperty("mlurl");
		String url = ml_url + "/recipe/publish/tag";
		String type = "POST";
		
		response_json = httpConfig.callApi(json_object, url, type);
//		String ingredient_data = "{\r\n"
//				+ "  \"result\": [{\r\n"
//				+ "      \"ingredient_code\": \"4000001\",\r\n"
//				+ "      \"ingredient_name\": \"소고기한우등식\",\r\n"
//				+ "      \"ingredient_category\": 1,\r\n"
//				+ "      \"ingredient_main_servingsize\": 100,\r\n"
//				+ "      \"ingredient_main_countunit\": \"g\",\r\n"
//				+ "      \"ingredient_countunit\": [\"팩\", \"근\"],\r\n"
//				+ "      \"ingredient_limit_byage\": 12,\r\n"
//				+ "      \"search_category\": [{\r\n"
//				+ "          \"search_category_code\": 4,\r\n"
//				+ "          \"search_category_name\": \"육류\"\r\n"
//				+ "        }\r\n"
//				+ "      ],\r\n"
//				+ "      \"allergy\": [{\r\n"
//				+ "          \"allergy_ingredient_code\": 1,\r\n"
//				+ "          \"allergy_ingredient_name\": \"우유\",\r\n"
//				+ "          \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/milk.jpg\"\r\n"
//				+ "        }\r\n"
//				+ "      ]\r\n"
//				+ "    }\r\n"
//				+ "  ]\r\n"
//				+ "}";
//		JSONParser ingredient_parser = new JSONParser();
//		JSONObject json_ingredient_obj = (JSONObject) ingredient_parser.parse(ingredient_data);
//		
//		JSONArray json_ingredient_array = (JSONArray) json_ingredient_obj.get("result");
//		List<Map<String, Object>> recipe_ingredient_list = new ArrayList<Map<String, Object>>();
//      logger.info("recipe_base_ingredient_list : " + recipe_ingredient_list);
        logger.info("====================== listRecipeBaseIngredient service end ======================");
        return response_json;
    }
	
	// 레시피 재료 조회
	@SuppressWarnings("unchecked")
	public JSONObject listRecipeIngredient(String text) throws ParseException, FileNotFoundException, IOException {
		logger.info("====================== listRecipeBaseIngredient service start ======================");
		
		// 베이스 재료 조회(ML 영역 호출)
		HttpConfig httpConfig = new HttpConfig();
		JSONObject json_object = new JSONObject();
		JSONObject response_json = null;
		json_object.put("text", text);
		
		Properties properties = new Properties();
		URL property_url = new URL(properties_url);
        properties.load(property_url.openStream());
        
        String ml_url = properties.getProperty("mlurl");
		String url = ml_url + "/recipe/publish/search";
		String type = "POST";
		
		response_json = httpConfig.callApi(json_object, url, type);
//			String ingredient_data = "{\r\n"
//					+ "  \"result\": [{\r\n"
//					+ "      \"ingredient_code\": \"4000001\",\r\n"
//					+ "      \"ingredient_name\": \"소고기한우등식\",\r\n"
//					+ "      \"ingredient_category\": 1,\r\n"
//					+ "      \"ingredient_main_servingsize\": 100,\r\n"
//					+ "      \"ingredient_main_countunit\": \"g\",\r\n"
//					+ "      \"ingredient_countunit\": [\"팩\", \"근\"],\r\n"
//					+ "      \"ingredient_limit_byage\": 12,\r\n"
//					+ "      \"search_category\": [{\r\n"
//					+ "          \"search_category_code\": 4,\r\n"
//					+ "          \"search_category_name\": \"육류\"\r\n"
//					+ "        }\r\n"
//					+ "      ],\r\n"
//					+ "      \"allergy\": [{\r\n"
//					+ "          \"allergy_ingredient_code\": 1,\r\n"
//					+ "          \"allergy_ingredient_name\": \"우유\",\r\n"
//					+ "          \"image_url\": \"https://s3.ap-northeast-2.amazonaws.com/mybucket/milk.jpg\"\r\n"
//					+ "        }\r\n"
//					+ "      ]\r\n"
//					+ "    }\r\n"
//					+ "  ]\r\n"
//					+ "}";
//			JSONParser ingredient_parser = new JSONParser();
//			JSONObject json_ingredient_obj = (JSONObject) ingredient_parser.parse(ingredient_data);
//			
//			JSONArray json_ingredient_array = (JSONArray) json_ingredient_obj.get("result");
//			List<Map<String, Object>> recipe_ingredient_list = new ArrayList<Map<String, Object>>();
//	        logger.info("recipe_base_ingredient_list : " + recipe_ingredient_list);
        logger.info("====================== listRecipeBaseIngredient service end ======================");
        return response_json;
    }
	
	// 레시피 업로드
	@SuppressWarnings("unchecked")
	public int recipeUpload(String recipe_key) throws org.json.simple.parser.ParseException, FileNotFoundException, IOException {
		logger.info("====================== recipeUpload service start ======================");
		
		int result = 0;
		
		List<Map<String, Object>> recipe_ingredient_list = new ArrayList<Map<String, Object>>();
		Recipe recipe = new Recipe();
		recipe.setRecipe_key(recipe_key);
		recipe = recipeDetailMapper.selectRecipeDetail(recipe);
		ArrayList<RecipeIngredient> recipe_ingredient_arr_list = recipeDetailMapper.selectRecipeIngredient(recipe_key);
		
		for(int i = 0; i < recipe_ingredient_arr_list.size(); i++) {
			LinkedHashMap<String, Object> recipe_ingredient_map = new LinkedHashMap<String, Object>();
			RecipeIngredient recipe_ingredient_arr = recipe_ingredient_arr_list.get(i);
			recipe_ingredient_map.put("recipe_ingredient_key", recipe_ingredient_arr.getRecipe_ingredient_key());
			recipe_ingredient_map.put("recipe_ingredient_name", recipe_ingredient_arr.getRecipe_ingredient_name());
			recipe_ingredient_map.put("recipe_ingredient_category", recipe_ingredient_arr.getRecipe_ingredient_category());
			recipe_ingredient_map.put("recipe_ingredient_search_categoy", recipe_ingredient_arr.getRecipe_ingredient_main_category());
			recipe_ingredient_map.put("recipe_ingredient_amount", recipe_ingredient_arr.getRecipe_ingredient_amount());
			recipe_ingredient_map.put("recipe_ingreident_countunit", recipe_ingredient_arr.getRecipe_ingredient_countunit());
			
			recipe_ingredient_list.add(recipe_ingredient_map);
		}
		
		// 건강태그, 칼로리, 건강발달 업데이트(ML 영역 호출)
		HttpConfig httpConfig = new HttpConfig();
		JSONObject json_object = new JSONObject();
		JSONObject response_json = null;
		json_object.put("recipe_key", recipe.getRecipe_key());
		json_object.put("recipe_category", recipe.getRecipe_category());
		json_object.put("recipe_servingsize", recipe.getRecipe_servings());
		json_object.put("created_datetime", recipe.getCreated_datetime());
		json_object.put("last_modified_datetime", recipe.getLast_modified_datetime());
		json_object.put("recipe_ingredient", recipe_ingredient_list.toString());
		
		Properties properties = new Properties();
		URL property_url = new URL(properties_url);
        properties.load(property_url.openStream());
        
        String ml_url = properties.getProperty("mlurl");
		String url = ml_url + "/recipe/publish/info";
		String type = "POST";
		
		response_json = httpConfig.callApi(json_object, url, type);
//		String health_data = "{\r\n"
//				+ "  \"recipe_key\": \"300001\",\r\n"
//				+ "  \"recipe_category\": \"밥/죽\",\r\n"
//				+ "  \"created_datetime\": \"2022-18-13 10:15:25\",\r\n"
//				+ "  \"recipe_health_tag\": [\"식이섬유풍부\", \"칼슘풍부\"],\r\n"
//				+ "  \"recipe_total_calory\": 500,\r\n"
//				+ "  \"health\": [{\r\n"
//				+ "    \"health_code\": \"HINT000001\",\r\n"
//				+ "    \"health_name\": \"모발\",\r\n"
//				+ "  }]\r\n"
//				+ "}";
//		JSONParser health_parser = new JSONParser();
//		JSONObject json_health_obj = (JSONObject) health_parser.parse(health_data);
		
		JSONArray recipe_health_tag = (JSONArray) response_json.get("recipe_health_tag");
		for(int i = 0; i < recipe_health_tag.size(); i++) {
			recipe.setRecipe_tag_no(i + 1);
			recipe.setRecipe_tag_desc(recipe_health_tag.get(i).toString());
			recipe.setRecipe_tag_type("건강태그");
			
			recipeRegistMapper.insertRecipeTag(recipe);
		}
		recipe.setRecipe_cal(Integer.parseInt(response_json.get("recipe_total_calory").toString()));
		JSONArray json_health_arr = (JSONArray) response_json.get("health");
		String health_name = "";
		String[] health_arr = new String[json_health_arr.size()];
		
		for(int i = 0; i < json_health_arr.size(); i++) {
			JSONObject json_health_obj = (JSONObject) json_health_arr.get(i);
			
			health_name = json_health_obj.get("health_name").toString();
			health_arr[i] = health_name;
		}
		recipe.setRecipe_health_develop(Arrays.toString(health_arr));
		
		result = recipeRegistMapper.updateRecipeAnalysis(recipe);
		
		if(result > 0) {
			recipe.setRecipe_check_status("검수중");
			recipe.setRecipe_temp_step("완료");
			recipe.setRecipe_key(recipe_key);
			recipeRegistMapper.updateRecipeStatus(recipe);
		}
		
		logger.info("====================== recipeUpload service end ======================");
		return result;
	}
}
