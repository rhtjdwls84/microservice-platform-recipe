package com.kyobo.platform.recipe.config;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class AWSConfig {
	Region region = Region.AP_NORTHEAST_2;
	EnvironmentVariableCredentialsProvider aws_credential = EnvironmentVariableCredentialsProvider.create();
	
	public S3Client awsS3Client() {
		//Amazon S3
		S3Client s3Client = S3Client.builder()
				.region(region)
				.credentialsProvider(aws_credential)
				.build();
		return s3Client;
	}
}
