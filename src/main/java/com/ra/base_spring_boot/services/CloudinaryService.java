package com.ra.base_spring_boot.services;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {
@Autowired
    private Cloudinary cloudinary;
public String upload(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    if (fileName != null && fileName.contains(".")) {
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
    }
    Map uploadParams = com.cloudinary.utils.ObjectUtils.asMap(
            "public_id", fileName
    );
    try {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
        return uploadResult.get("secure_url").toString();
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}
