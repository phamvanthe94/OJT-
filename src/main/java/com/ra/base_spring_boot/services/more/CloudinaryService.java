package com.ra.base_spring_boot.services.more;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file) {
        String fileName = extractPublicId(file);
        Map<String, Object> uploadParams = new HashMap<>();
        if (fileName != null) {
            uploadParams.put("public_id", fileName);
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            Object secureUrl = uploadResult.get("secure_url");
            return secureUrl != null ? secureUrl.toString() : null;
        } catch (IOException e) {
            log.error("Cloudinary upload failed for file {}", file.getOriginalFilename(), e);
            return null;
        }
    }

    private String extractPublicId(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            return null;
        }
        int extensionIndex = fileName.lastIndexOf('.');
        return extensionIndex > 0 ? fileName.substring(0, extensionIndex) : fileName;
    }
}
