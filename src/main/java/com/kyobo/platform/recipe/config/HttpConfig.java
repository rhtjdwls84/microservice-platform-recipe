package com.kyobo.platform.recipe.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ch.qos.logback.classic.Logger;

public class HttpConfig {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpConfig.class);
 
    public JSONObject callApi(JsonObject jsonObject, String target_url, String type){
        HttpURLConnection conn = null;
        JSONObject responseJson = null;
        
        try {
            //URL 설정
            URL url = new URL(target_url);
 
            conn = (HttpURLConnection) url.openConnection();
            
            // type의 경우 POST, GET, PUT, DELETE 가능
            conn.setRequestMethod(type);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Transfer-Encoding", "chunked");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setDoOutput(true);
            
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            // JSON 형식의 데이터 셋팅
//            JsonObject commands = new JsonObject();
//            JsonArray jsonArray = new JsonArray();
//            
//            params.addProperty("key", 1);
//            params.addProperty("age", 20);
//            params.addProperty("userNm", "홍길동");
// 
//            commands.add("userInfo", params);
//             // JSON 형식의 데이터 셋팅 끝
//            
//            // 데이터를 STRING으로 변경
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String jsonOutput = gson.toJson(commands);
                 
            bw.write(jsonObject.toString());
            bw.flush();
            bw.close();
            
            // 보내고 결과값 받기
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
//                responseJson = (JSONObject) sb.toString();
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(sb.toString());
                responseJson = (JSONObject) obj;
                
                // 응답 데이터
                logger.info("responseJson :: " + responseJson);
            } 
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			e.printStackTrace();
		}
		return responseJson;
    }
}
