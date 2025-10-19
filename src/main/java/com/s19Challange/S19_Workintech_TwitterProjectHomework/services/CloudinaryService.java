package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile multipartFile);
    void deleteImage(String publicId);
}
