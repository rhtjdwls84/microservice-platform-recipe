package com.kyobo.platform.recipe.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.kyobo.platform.recipe.answer.Answer;
import com.kyobo.platform.recipe.config.GlobalExceptionHandler;
import com.kyobo.platform.recipe.redis.RedisUser;
//import com.kyobo.platform.recipe.user.UserRepository;

import ch.qos.logback.classic.Logger;
import groovy.util.logging.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Controller
@Slf4j
public class HelloController {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(HelloController.class);
	
//	@Autowired
//    private UserRepository userRepository;
//	
//	
//	@RequestMapping("/kyobo2")
//	@ResponseBody
//	public Answer kyobo() throws Exception {
//		Answer answer = new Answer();
//		answer.setId(1);
//		answer.setContent("test");
//		
//		throw new GlobalExceptionHandler();
////		return answer;
//	}
	
	@RequestMapping(value = "/profile", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
    public String profile(@RequestParam String access_token) throws ParseException, UnsupportedEncodingException {
//		String token = "AAAAOjQsCyjMtuYUFRI707jleWdKsDsSRMrJncaBHmpvtcC-L3R0Iz7YYDUKI-SR-EZl3v6byMaZbIgsbh9lanvudHg"; // ????????? ????????? ?????? ??????;
        String header = "Bearer " + access_token; // Bearer ????????? ?????? ??????

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        
        String responseBody = get(apiURL, requestHeaders);
        System.out.println(responseBody);
        
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(responseBody.toString());
        JSONObject jsonObj = (JSONObject) obj;
        String response = jsonObj.get("response").toString();
        
        Object objName = parser.parse(response.toString());
        JSONObject nameObj = (JSONObject) objName;
        String strName = nameObj.get("name").toString();
        logger.debug(strName);
        
        return responseBody;
    }
	
	private static String get(String apiUrl, Map<String, String> requestHeaders) throws ParseException {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            
            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) { // ?????? ??????
                return readBody(con.getInputStream());
            } else { // ?????? ??????
                return readBody(con.getErrorStream());
            }
        } catch(IOException e) {
            throw new RuntimeException("API ????????? ?????? ??????", e);
        } finally {
            con.disconnect();
        }
    }
	
	private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch(MalformedURLException e) {
            throw new RuntimeException("API URL??? ?????????????????????. : " + apiUrl, e);
        } catch(IOException e) {
            throw new RuntimeException("????????? ??????????????????. : " + apiUrl, e);
        }
    }
	
	private static String readBody(InputStream body) throws UnsupportedEncodingException, ParseException {
        InputStreamReader streamReader = new InputStreamReader(body, "UTF-8");
        
        try(BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            
            String line;
            while((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            
            String res_body = convertString(responseBody.toString());
            return res_body;
        } catch(IOException e) {
            throw new RuntimeException("API ????????? ????????? ??????????????????.", e);
        }
    }
	
	// ?????????????????? String?????? ??????
	public static String convertString(String val) {  
		// ????????? ????????? ????????? ?????? ??????  
		StringBuffer sb = new StringBuffer();  
		// ????????? ???????????? ????????????.  
		for(int i = 0; i < val.length(); i++) {
			// ????????? \ u??? ???????????? 6????????? ????????????.  
			if('\\' == val.charAt(i) && 'u' == val.charAt(i + 1)) {      
				// ??? ??? ???????????? ??????????????? 16?????? ????????????. int????????? ???????????? ?????? char ???????????? ?????? ????????????.      
				Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);      
				// ????????? ????????? ????????? ?????????.      
				sb.append(r);      
				// for??? ?????? ??? 1??? 5??? ?????? 6????????? ??????      
				i += 5;    
			} else {      
				// ascii????????? ????????? ????????? ?????????.      
				sb.append(val.charAt(i));    
			}  
		}  
		// ?????? ??????  
		return sb.toString();
	}
	
//	@RequestMapping("/login")
//    public String naverConnect() throws UnsupportedEncodingException {
//        // state??? ?????? ??????
//        SecureRandom random = new SecureRandom();
//        String state = new BigInteger(130, random).toString(32);
//        
//        // redirect
//        StringBuffer url = new StringBuffer();
//        url.append("https://nid.naver.com/oauth2.0/authorize?");
//        url.append("client_id=" + "OXHUm8glFJZS0lfME8xa");
//        url.append("&response_type=code");
//        url.append("&redirect_uri=http://localhost:8080/callback");
//        url.append("&state=" + state);
//        
//        return "redirect:" + url;
//    }
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(@RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
		String clientId = "OXHUm8glFJZS0lfME8xa"; //?????????????????? ??????????????? ????????????";
	    String clientSecret = "70VaSqDJ8U"; //?????????????????? ??????????????? ????????????";
	    String redirectURI = URLEncoder.encode("YOUR_CALLBACK_URL", "UTF-8");
	    String apiURL;
	    apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
	    apiURL += "client_id=" + clientId;
	    apiURL += "&client_secret=" + clientSecret;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&code=" + code;
	    apiURL += "&state=" + state;

	    StringBuffer res = new StringBuffer();
	    System.out.println("apiURL : " + apiURL);
	    
	    String access_token = "";
	    
	    try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.println("responseCode : " + responseCode);
			if(responseCode == 200) { // ?????? ??????
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {  // ?????? ??????
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			
			while((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			if(responseCode == 200) {
				System.out.println(res.toString());
				//2. Parser
		        JSONParser jsonParser = new JSONParser();
		        
		        //3. To Object
		        Object obj = jsonParser.parse(res.toString());
		        
		        //4. To JsonObject
		        JSONObject jsonObj = (JSONObject) obj;
		        
				System.out.println(jsonObj.get("access_token"));
				access_token = (String) jsonObj.get("access_token");
			}
	    } catch(Exception e) {
	    	System.out.println(e);
	    }
	    return "redirect:/profile?access_token=" + access_token;
	} 
	
//	//?????? ????????? ?????? ??????
//	@RequestMapping(value = "/searchRecipeList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	@GetMapping("/searchRecipeList/{search_text}/{search_type}")
//	@Cacheable(value = "post-single", key = "#user_id", cacheManager = "cacheManager")
//    public String searchRecipeList(@RequestParam(value = "search_text", required = true) String search_text, 
//    		@RequestParam(value = "search_type", required = false) String search_type) {
//		logger.info("====================== recipeList start ======================");
//		//Redis ????????????
////			Optional<RedisUser> redisUser = redisService.redisGetSession(id);
//		
//		String jsonRecipeList = null;
////			if(redisUser != null) {
//			//?????? ??????
//			List<RecipeList> recipeList = recipeListService.getSearchRecipeList(search_text, search_type);
//			Gson gson = new Gson();
//			jsonRecipeList = gson.toJson(recipeList);
////			}
//		logger.info("====================== recipeList end ======================");
//        return jsonRecipeList;     
//    }
}
