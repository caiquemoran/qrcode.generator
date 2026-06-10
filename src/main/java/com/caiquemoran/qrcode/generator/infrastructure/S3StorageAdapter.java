package com.caiquemoran.qrcode.generator.infrastructure;

import com.caiquemoran.qrcode.generator.ports.StoragePort;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class S3StorageAdapter implements StoragePort {

    private final MinioClient minioClient;
    private final String bucketName;
    private final String url;

    public S3StorageAdapter(
            @Value("${minio.url:http://localhost:9000}") String url,
            @Value("${minio.access.key}") String accessKey,
            @Value("${minio.secret.key}") String secretKey,
            @Value("${minio.bucket.name:minio}") String bucketName) {
        
        this.url = url;
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
        
        try {
            boolean found = this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing MinIO bucket", e);
        }
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, fileData.length, -1)
                            .contentType(contentType)
                            .build()
            );
            return String.format("%s/%s/%s", this.url, this.bucketName, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }
}
