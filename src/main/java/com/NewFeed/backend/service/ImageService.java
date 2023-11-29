package com.NewFeed.backend.service;

import com.NewFeed.backend.modal.Imageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    public void save(Imageable imageable, MultipartFile multipartFile) throws IOException;
}
