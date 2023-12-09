package com.NewFeed.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    private S3Bucket s3Bucket;
    public AwsProperties(){
        s3Bucket = new S3Bucket();
    }
    @Getter
    @Setter
    public static class S3Bucket{
      private String region;
      private String name;
    }
}
