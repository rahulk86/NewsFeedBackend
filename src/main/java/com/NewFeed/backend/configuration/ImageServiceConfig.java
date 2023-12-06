package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.modal.Image;
import com.NewFeed.backend.repository.ImageFileSystemRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class ImageServiceConfig {
    @Autowired
    private ImageFileSystemRepository imageFileSystemRepository;
    @Bean
    public ModelMapper userImageServiceModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<Image, String > ImageToByte
                = context-> {
            try {
                byte[] content = imageFileSystemRepository.findByImageFileSystem(context.getSource()).getContentAsByteArray();
                return "data:" + context.getSource().getContentType() + ";base64," +
                        Base64.getEncoder().encodeToString(content);
            }
            catch (Exception e) {
                return context. getSource().getName();
            }
        };

        modelMapper.typeMap(Image.class, ImageDto.class).addMappings(mapper -> {
            mapper.using(ImageToByte).map(image ->image,ImageDto::setUrl);
        });
        return modelMapper;
    }
}
