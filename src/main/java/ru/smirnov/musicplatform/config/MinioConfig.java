package ru.smirnov.musicplatform.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("#{'${minio.buckets}'.split(',')}")
    private List<String> buckets;

    // TRY WITH RESOURCES
    @Bean
    public MinioClient buildMinioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(this.endpoint)
                .credentials(this.accessKey, this.secretKey)
                .build();

        this.buckets.forEach(bucket -> {
            try {
                this.createBucketIfNotExists(minioClient, bucket);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return minioClient;
    }

    // НАДО БОЛЕЕ КОНКРЕТНО ПООТЛАВЛИВАТЬ ОШИБКИ
    private void createBucketIfNotExists(MinioClient minioClient, String bucketName) throws Exception {

        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!exists)
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

        // Пример установки политики "чтение-запись"
        // MinIO использует политики в формате JSON. Вот пример простой политики,
        // которая разрешает полный доступ к бакету для всех (в реальных условиях нужно ограничивать доступ).
        String policyJson = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": {\"AWS\": [\"*\"]},\n" +
                "      \"Action\": [\n" +
                "        \"s3:GetBucketLocation\",\n" +
                "        \"s3:ListBucket\"\n" +
                "      ],\n" +
                "      \"Resource\": [\n" +
                "        \"arn:aws:s3:::" + bucketName + "\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": {\"AWS\": [\"*\"]},\n" +
                "      \"Action\": [\n" +
                "        \"s3:GetObject\",\n" +
                "        \"s3:PutObject\",\n" +
                "        \"s3:DeleteObject\"\n" +
                "      ],\n" +
                "      \"Resource\": [\n" +
                "        \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Установка политики на бакет
        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJson)
                        .build()
        );
    }

}
