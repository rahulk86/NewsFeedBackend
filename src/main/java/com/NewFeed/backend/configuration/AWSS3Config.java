package com.NewFeed.backend.configuration;

import com.NewFeed.backend.exception.AmazonS3WithBucketException;
import com.amazonaws.auth.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class AWSS3Config {

    @Autowired
    private AwsProperties awsProperties;

    private AWSCredentials getAWSCredentials(){
        EnvironmentVariableCredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
        return provider.getCredentials();
    }
    @Bean
    public AmazonS3Bucket getS3Client() {
        return AmazonS3Bucket
                .builder()
                .amazonS3(AmazonS3ClientBuilder
                        .standard()
                        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                        .withRegion(awsProperties.getS3Bucket().getRegion())
                        .build())
                .bucket(awsProperties.getS3Bucket().getName())
                .build();
    }
    @Getter
    @Builder
    public static class AmazonS3Bucket {

        private String bucket;
        private String key;
        private AmazonS3 amazonS3;

        public void putObject(MultipartFile multipartFile,String folder) throws IOException {
            try (InputStream inputStream = multipartFile.getInputStream()) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(multipartFile.getContentType());
                this.key = generateKey(multipartFile.getOriginalFilename(),folder);
                amazonS3.putObject(bucket,key, inputStream,objectMetadata);
            }
        }

        public String getUrl(){
            if(key==null || key.isBlank()){
              throw new AmazonS3WithBucketException("please put object first");
            }
            return amazonS3.getUrl(bucket, key).toExternalForm();
        }

        private String generateKey(String originalFilename,String folder) {
            return folder+"/"+System.currentTimeMillis() + "_" + originalFilename;
        }

    }
}
