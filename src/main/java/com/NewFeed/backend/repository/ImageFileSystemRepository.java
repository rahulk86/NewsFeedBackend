package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Repository
public class ImageFileSystemRepository {
    @Autowired
    ResourceLoader resourceLoader;
    @Value("${bezkoder.app.path}")
    private String filePath;
    public FileSystemResource findByImageFileSystem(Image image){
        File file = new File(filePath+File.separator+image.getImageableType()+
                              File.separator+image.getId().toString()+File.separator+image.getName());
       return new FileSystemResource(file);
    }
    public void save(Image image, byte[] content) throws IOException {
        String absolutePath = filePath+File.separator+image.getImageableType()+File.separator +image.getId().toString();
        File absoluteFile = new File(absolutePath);
        if (!absoluteFile.exists()) {
            absoluteFile.mkdirs();
        }
        File file = new File(absoluteFile, image.getName());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content);
        fos.flush();
        fos.close();
    }
}
