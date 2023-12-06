package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.modal.Image;
import com.NewFeed.backend.modal.Imageable;
import com.NewFeed.backend.repository.ImageFileSystemRepository;
import com.NewFeed.backend.repository.ImageRepository;
import com.NewFeed.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Transactional
@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageFileSystemRepository imageFileSystemRepository;

    @Autowired
    private ImageRepository imageRepository;


    @Override
    public void save(Imageable imageable, MultipartFile multipartFile) throws IOException {
        Image image =  imageRepository
                .findByImageableTypeAndImageableId(imageable
                                                     .getClass()
                                                     .getSimpleName(),
                                                   imageable.getId())
                .orElse(null);
        if(image!=null && image.getActive()==1){
            image.setActive(0);
            imageRepository.save(image);
        }
        Image newImage = new Image();
        newImage.setName(multipartFile.getOriginalFilename());
        newImage.setContentType(multipartFile.getContentType());
        newImage.setImageableId(imageable.getId());
        newImage.setImageableType(imageable.getClass().getSimpleName());
        newImage.setActive(1);
        newImage.setCreateAt(LocalDateTime.now());
        imageFileSystemRepository.save(imageRepository.save(newImage),multipartFile.getInputStream().readAllBytes());
    }
}
