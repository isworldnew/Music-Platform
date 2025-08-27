package ru.smirnov.musicplatform.service.minio;

import io.minio.*;
import io.minio.errors.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Async @SneakyThrows
    public void uploadObjectWithMetadata(String bucketName, String objectName, MultipartFile object, Map<String, String> objectMetaData) {

        try (InputStream inputStream = object.getInputStream()) {
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
        // пока вот такой костыль, надо будет потом это поправить
        catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }

    }

    @Async @SneakyThrows
    public void removeObject(String bucketName, String objectName) {
        this.minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    @Async @SneakyThrows
    public void replaceObjectInBucket(String bucketName, String oldObjectName, String newObjectName) {
        // кстати, тут как с транзакциями: я вызываю @Async метод из другого @Async метода того же класса
        // поэтому вызовется не прокси, а this
        // и оба метода выполнятся в одном потоке

        this.minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newObjectName)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(oldObjectName)
                                        .build()
                        )
                        .build()
        );

        this.removeObject(bucketName, oldObjectName);

        /*
        io.minio.errors.ErrorResponseException: This copy request is illegal because it is trying to copy an object to itself without changing the object's metadata, storage class, website redirect location or encryption attributes.
        at io.minio.S3Base$1.onResponse(S3Base.java:789) ~[minio-8.5.17.jar:8.5.17]
        at io.minio.S3Base$1.onResponse(S3Base.java:625) ~[minio-8.5.17.jar:8.5.17]
        at okhttp3.internal.connection.RealCall$AsyncCall.run(RealCall.kt:519) ~[okhttp-4.12.0.jar:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
        */
    }

    @Async @SneakyThrows
    public void replaceObjectInBucket(String bucketName, String objectName, MultipartFile object) {
        this.removeObject(bucketName, objectName);
        this.uploadObjectWithMetadata(bucketName, objectName, object, null);
    }

}
