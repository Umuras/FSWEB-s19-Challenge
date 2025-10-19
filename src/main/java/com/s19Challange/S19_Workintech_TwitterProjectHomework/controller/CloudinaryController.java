package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class CloudinaryController {

    private CloudinaryService cloudinaryService;

    @Autowired
    public CloudinaryController(CloudinaryService cloudinaryService)
    {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public String imageUpload(@RequestParam("file") MultipartFile file)
    {
        return cloudinaryService.uploadImage(file);
    }

    @DeleteMapping
    public void deleteImage(@RequestParam String publicId)
    {
        cloudinaryService.deleteImage(publicId);
    }
}
