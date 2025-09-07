package ru.smirnov.musicplatform.service.implementation.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;

import java.io.InputStream;
import java.util.Map;

@Service
public class MinioServiceImplementation implements MinioService {

    private final MinioClient minioClient;

    @Autowired
    public MinioServiceImplementation(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void uploadObjectWithMetadata(String bucketName, String objectName, MultipartFile object, Map<String, String> objectMetaData) throws Exception {
        InputStream inputStream = object.getInputStream();
        this.minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, object.getSize(), -1)
                        .contentType(object.getContentType())
                        .userMetadata(objectMetaData)
                        .build()
        );
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        this.minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    @Override
    public void replaceObjectInBucket(String bucketName, String objectName, MultipartFile object) throws Exception {
        this.removeObject(bucketName, objectName);
        this.uploadObjectWithMetadata(bucketName, objectName, object, null);
    }
}
