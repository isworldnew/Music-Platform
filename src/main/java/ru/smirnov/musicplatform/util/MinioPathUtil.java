package ru.smirnov.musicplatform.util;

import org.springframework.stereotype.Component;

@Component
public class MinioPathUtil {

    public String extractBucketName(String reference) {
        return reference.split("/")[0];
    }

    public String extractObjectName(String reference) {
        return reference.split("/")[1];
    }

    public String generateFormattedReference(String bucketName, String objectNameWithBlanks) {
        return (bucketName + "/" + (objectNameWithBlanks.replace(" ", "_")));
    }

}
