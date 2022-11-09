package com.kyobo.platform.recipe.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import java.text.ParseException;
import java.util.Properties;


import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import ch.qos.logback.classic.Logger;

@Configuration
public class AWSConfig {
	
	@Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;
    
    AmazonS3 amazonS3 = null;
    
	/* 사전작업
     * SecretAccess pem key를 아래 명령어로 DER 파일로 변환시킨 후 privateKeyFilePath 경로에 추가한다.
     * openssl pkcs8 -topk8 -nocrypt -in origin.pem -inform PEM -out new.der -outform DER
     */
    private String privateKeyFilePath = "src\\main\\resources\\pk8-APKAZ3MKOJFETMDPAWV6.der";
    
    private byte[] derPrivateKey;
    
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AWSConfig.class);
    
    @Bean
    //s3 사용을 위한 인증
	public AmazonS3 amazonS3() {
    	logger.info("====================== amazonS3 start ======================");
    	AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    	
    	//Amazon S3
    	amazonS3 = AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
    	
    	logger.info("====================== amazonS3 end ======================");
    	
    	return amazonS3;
	}
    
    public void CloudFrontManager() throws IOException {
    	logger.info("Signed Url privateKeyFilePath ====================== {} =========================", privateKeyFilePath);
        derPrivateKey = ServiceUtils.readInputStreamToBytes(new FileInputStream(privateKeyFilePath));
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    
    // 미리 준비된 정책
    public String createSignedUrlCanned(String s3FileName) 
    		throws ParseException, CloudFrontServiceException, FileNotFoundException, IOException {
    	logger.info("====================== createSignedUrlCanned start ======================");
    	//cloudfront 사용을 위한 인증 properties 파일 로드
    	Properties properties = new Properties();
        properties.load(new FileInputStream("src\\main\\resources\\awsAuth.properties"));
        
        String distributionDomain = properties.getProperty("distributionDomain");
        String keyPairId = properties.getProperty("keyPairId");
        
        String policyResourcePath = "http://" + distributionDomain + "/" + s3FileName;
        
        String signedUrlCanned = CloudFrontService.signUrlCanned(
        		policyResourcePath, // Resource URL or Path
                keyPairId,     // Certificate identifier,
                derPrivateKey, // DER Private key data
                ServiceUtils.parseIso8601Date("2020-11-14T22:20:00.000Z") // DateLessThan
        );
        logger.info("Signed Url Canned ====================== {} =========================", signedUrlCanned);
        logger.info("====================== createSignedUrlCanned end ======================");

        return signedUrlCanned;
    }
}
