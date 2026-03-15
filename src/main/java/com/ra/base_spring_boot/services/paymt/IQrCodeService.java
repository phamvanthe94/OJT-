package com.ra.base_spring_boot.services.paymt;

public interface IQrCodeService {


    byte[] generateQrCode(String content, int width, int height);


    default byte[] generateQrPng(String content, int size) {
        return generateQrCode(content, size, size);
    }
}
