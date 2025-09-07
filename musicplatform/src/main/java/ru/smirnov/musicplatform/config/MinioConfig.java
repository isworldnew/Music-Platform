package ru.smirnov.musicplatform.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    // @Value("#{'${minio.buckets}'.split(',')}")
    // private List<String> buckets;

    // TRY WITH RESOURCES
    @Bean
    public MinioClient buildMinioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(this.endpoint)
                .credentials(this.accessKey, this.secretKey)
                .build();

        Arrays.stream(MinioBuckets.values()).forEach(bucket -> {
            try {
                this.createBucketIfNotExists(minioClient, bucket.getBucketName());
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

        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

    }

}
