package com.kyobo.platform.recipe.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Security;
import java.text.ParseException;
import java.util.Properties;


import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import ch.qos.logback.classic.Logger;

@Configuration
public class AWSConfig {
	
    AmazonS3 amazonS3 = null;
    
	/* 사전작업
     * SecretAccess pem key를 아래 명령어로 DER 파일로 변환시킨 후 privateKeyFilePath 경로에 추가한다.
     * openssl genrsa -out pk8-APKAZ3MKOJFETMDPAWV6.pem 2048
     * openssl rsa -pubout -in pk8-APKAZ3MKOJFETMDPAWV6.pem -out rsa-APKAZ3MKOJFETMDPAWV6.pem
     * openssl pkcs8 -topk8 -nocrypt -in origin.pem -inform PEM -out new.der -outform DER
     */
    private String properties_url = "https://d3am0bqv86scod.cloudfront.net/auth/awsAuth.properties";
    
    private byte[] derPrivateKey;
    
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AWSConfig.class);
    
    //s3 사용을 위한 인증
	public AmazonS3 amazonS3(String region) throws FileNotFoundException, IOException {
    	logger.info("====================== amazonS3 start ======================");
    	// 인증 properties 파일 로드
    	Properties properties = new Properties();
    	URL url = new URL(properties_url);
        properties.load(url.openStream());
        
        String accessKey = properties.getProperty("accessKey");
        String secretKey = properties.getProperty("secretKey");

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    	
    	//Amazon S3
    	amazonS3 = AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
    	
    	logger.info("====================== amazonS3 end ======================");
    	
    	return amazonS3;
	}
    
    public void CloudFrontManager() throws IOException, URISyntaxException {
    	logger.info("====================== CloudFrontManager start ======================");
    	
    	Properties properties = new Properties();
    	URL url = new URL(properties_url);
    	properties.load(url.openStream());
        
        String privateKeyFile = properties.getProperty("privateKeyFile");
        String privateKeyUrl = properties.getProperty("privateKeyUrl");
        logger.info("privateKeyFile ====================== {} =========================", privateKeyFile);
        
        URL private_key_url = new URL(privateKeyUrl);
//        derPrivateKey = ServiceUtils.readInputStreamToBytes(new FileInputStream(file));
        derPrivateKey = ServiceUtils.readInputStreamToBytes(private_key_url.openStream());
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        logger.info("====================== CloudFrontManager end ======================");
    }
    
    // 미리 준비된 정책
    public String createSignedUrlCanned(String s3FileName, String sign_time) 
    		throws ParseException, CloudFrontServiceException, FileNotFoundException, IOException {
    	logger.info("====================== createSignedUrlCanned start ======================");
    	// cloudfront 사용을 위한 인증 properties 파일 로드
    	Properties properties = new Properties();
    	URL url = new URL(properties_url);
    	properties.load(url.openStream());
        
        String distributionDomain = properties.getProperty("distributionDomain");
        String keyPairId = properties.getProperty("keyPairId");
        
        String policyResourcePath = "http://" + distributionDomain + "/" + s3FileName;
        
        String signedUrlCanned = CloudFrontService.signUrlCanned(
        		policyResourcePath, // Resource URL or Path
                keyPairId,     // Certificate identifier,
                derPrivateKey, // DER Private key data
                ServiceUtils.parseIso8601Date(sign_time) // DateLessThan
        );
        logger.info("Signed Url Canned ====================== {} =========================", signedUrlCanned);
        logger.info("====================== createSignedUrlCanned end ======================");

        return signedUrlCanned;
    }
    
    public String createCustomSingedUrl(String s3FileName, String sign_time, String signed_time) 
    		throws ParseException, CloudFrontServiceException, FileNotFoundException, IOException {
    	logger.info("====================== createCustomSingedUrl start ======================");
    	//cloudfront 사용을 위한 인증 properties 파일 로드
    	Properties properties = new Properties();
    	URL url = new URL(properties_url);
    	properties.load(url.openStream());
//        properties.load(new FileInputStream(pre_path + "awsAuth.properties"));
        
        String distributionDomain = properties.getProperty("distributionDomain");
        String keyPairId = properties.getProperty("keyPairId");
    	
    	String policyResourcePath = "http://" + distributionDomain + "/" + s3FileName;

        String policy = CloudFrontService.buildPolicyForSignedUrl(
                // Resource path (optional, can include '*' and '?' wildcards)
                policyResourcePath,
                // DateLessThan
                // 접근 만료시간 세팅
                ServiceUtils.parseIso8601Date(sign_time),
                // CIDR IP address restriction (optional, 0.0.0.0/0 means everyone)
                "0.0.0.0/0",
                // DateGreaterThan (optional)
                ServiceUtils.parseIso8601Date(signed_time)
        );

        // Generate a signed URL using a custom policy document.

        String signedUrl = CloudFrontService.signUrl(
                // Resource URL or Path
        		policyResourcePath,
                // Certificate identifier, an active trusted signer for the distribution
                keyPairId,
                // DER Private key data
                derPrivateKey,
                // Access control policy
                policy
        );

        logger.info("Signed Url By Custom Policy ====================== {} =========================", signedUrl);
        logger.info("====================== createCustomSingedUrl end ======================");
        return signedUrl;

    }
}
