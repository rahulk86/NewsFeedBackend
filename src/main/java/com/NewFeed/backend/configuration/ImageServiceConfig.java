package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.modal.Image;
import com.NewFeed.backend.repository.ImageFileSystemRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ImageServiceConfig {
    @Autowired
    private ImageFileSystemRepository imageFileSystemRepository;
    @Bean
    public ModelMapper userImageServiceModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<Image, byte[] > ImageToByte
                = c -> {
            try {
                return imageFileSystemRepository.findByImageFileSystem(c. getSource()).getContentAsByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        modelMapper.typeMap(Image.class, ImageDto.class).addMappings(mapper -> {
            mapper.using(ImageToByte).map(image ->image,ImageDto::setData);
        });
        return modelMapper;
    }
}
