package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository  extends JpaRepository<Image,Long> {


    Optional<Image> findByImageableTypeAndImageableId(String imageableType,Long imageableId);
}
