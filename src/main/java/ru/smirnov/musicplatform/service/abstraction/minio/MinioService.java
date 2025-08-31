package ru.smirnov.musicplatform.service.abstraction.minio;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MinioService {

    void uploadObjectWithMetadata(String bucketName, String objectName, MultipartFile object, Map<String, String> objectMetaData) throws Exception;

    void removeObject(String bucketName, String objectName) throws Exception;

    void replaceObjectInBucket(String bucketName, String objectName, MultipartFile object) throws Exception;
}
