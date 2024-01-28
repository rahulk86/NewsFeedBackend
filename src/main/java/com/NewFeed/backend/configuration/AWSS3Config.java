package com.NewFeed.backend.configuration;

import com.NewFeed.backend.exception.AWSS3ConfigException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

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
            if (multipartFile == null || folder == null) {
                return;
            }
            byte[] bytes = compressImage(multipartFile);
            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(multipartFile.getContentType());
                this.key = generateKey(multipartFile.getOriginalFilename(),folder);
                amazonS3.putObject(bucket,key, inputStream,objectMetadata);
            }
        }

        public String validateContentType(MultipartFile image) throws IOException {
            String contentType = image.getContentType();
            String[] separate = contentType.split("/");
            String imageType = separate[separate.length - 1];
            if ("jpeg".equalsIgnoreCase(imageType)
                || "jpg".equalsIgnoreCase(imageType)
                || "png".equalsIgnoreCase(imageType)) {
                return imageType;
            } else {
                throw new AWSS3ConfigException("AWSS3ConfigException image format: " + contentType);
            }

        }
        public byte[] compressImage(MultipartFile image) throws IOException{
            if (image == null) {
                return new byte[0];
            }
            String imageType                   = validateContentType(image);
            InputStream inputStream            = image.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            float imageQuality                 = 0.3f;
            BufferedImage bufferedImage        = ImageIO.read(inputStream);
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(imageType);

            if (!imageWriters.hasNext())
                throw new AWSS3ConfigException("Writers Not Found!!");

            ImageWriter imageWriter             = imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

            byte[] imageBytes = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();

            return imageBytes;
        }

        public String getUrl(){
            if(key==null || key.isBlank()){
              throw new AWSS3ConfigException("please put object first");
            }
            return amazonS3.getUrl(bucket, key).toExternalForm();
        }

        private String generateKey(String originalFilename,String folder) {
            return folder+"/"+System.currentTimeMillis() + "_" + originalFilename;
        }

    }
}
