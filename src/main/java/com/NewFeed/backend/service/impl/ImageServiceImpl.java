package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.configuration.AWSS3Config.AmazonS3Bucket;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.image.Imageable;
import com.NewFeed.backend.repository.image.ImageRepository;
import com.NewFeed.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional
@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AmazonS3Bucket amazonS3WithBucket;

    @Override
    @Transactional
    public void save(Imageable imageable, MultipartFile multipartFile) throws IOException {
        Image image =  imageRepository
                        .findByImageableTypeAndImageableId(imageable
                                                             .getClass()
                                                             .getSimpleName(),
                                                           imageable.getId())
                        .orElse(null);
        if(image!=null){
            imageRepository.save(image);
        }

        amazonS3WithBucket.putObject(multipartFile,imageable.getClass().getSimpleName()+" ID :"+imageable.getId());

        Image newImage = new Image();
        newImage.setUrl(amazonS3WithBucket.getUrl());
        newImage.setImageableId(imageable.getId());
        newImage.setImageableType(imageable.getClass().getSimpleName());
        imageRepository.save(newImage);
    }
}
