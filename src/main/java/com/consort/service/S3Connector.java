package com.consort.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;
import com.consort.entities.DomainModel;
import com.consort.util.EnvironmentContext;
import com.consort.util.EnvironmentProps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3Connector {
    final private static Logger logger = LoggerFactory.getLogger(DomainModelService.class);

    private String bucketName;
    private Gson gson = new Gson();

    public S3Connector() {
        this.bucketName = EnvironmentContext.getInstance().getenv(EnvironmentProps.BucketName);
    }

    public void initialize() {
        logger.info("Initializing S3Bucket '{}'", this.bucketName);
        AmazonS3 s3client = getS3Client();
        try {
            if (!s3client.doesBucketExist(this.bucketName)) {
                logger.info("Bucket '{}' does not yet exist, creating...", this.bucketName);
                Region reg = Region.EU_Frankfurt;
                s3client.createBucket(this.bucketName, reg);
                logger.info("Bucket '{}' created in region '{}'.", this.bucketName, reg);
            } else {
                logger.debug("Bucket '{}' already exists.", this.bucketName);
            }
        } catch (AmazonClientException ex) {
            logger.error("Could not create bucket '{}'", this.bucketName, ex);
            throw ex;
        }
    }

    private static AmazonS3 s3instance = null;

    private static AmazonS3 getS3Client() {
        if (s3instance == null) {
            String accessKey = EnvironmentContext.getInstance().getenv(EnvironmentProps.AccessKey);
            String secretKey = EnvironmentContext.getInstance().getenv(EnvironmentProps.SecretKey);

            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("eu-central-1").build();
            s3instance = s3client;
        }
        return s3instance;
    }

    public <T> T updateObject(String objectName, T value) {
        AmazonS3 s3client = getS3Client();
        String jsonString = this.gson.toJson(value);
        s3client.putObject(this.bucketName, objectName, jsonString);
        return value;
    }

    public <T> T getObject(String objectName) throws AmazonServiceException, SdkClientException {
        AmazonS3 s3client = getS3Client();
        String content = s3client.getObjectAsString(this.bucketName, objectName);
        if (content == null || content.isEmpty()) {
            return null;
        }
        Type jsonType = new TypeToken<List<DomainModel>>() {
        }.getType();
        return this.gson.fromJson(content, jsonType);
    }
}
