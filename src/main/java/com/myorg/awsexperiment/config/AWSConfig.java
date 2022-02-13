package com.myorg.awsexperiment.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.s3.proxyAddress}")
    private String proxyAddress;

    @Value("${aws.s3.proxyPort}")
    private String proxyPort;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    // TODO Get from external property
//    @Value("${aws.s3.region}")
//    private String region;

    Regions region = Regions.US_EAST_1;

    @Bean("setClient")
    protected AmazonS3 setClient() {

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

//        ClientConfiguration clientConfiguration = new ClientConfiguration(); // Or pass as parameters
//        clientConfiguration.setProxyHost(proxyAddress);
//        clientConfiguration.setProxyPort(proxyPort);

        // Read - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                // .withCredentials(new ProfileCredentialsProvider("experimentprofile")) // For using local and other credential provider
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) // for explicit setting of credentials
                .withRegion(region)
                //  .withClientConfiguration(clientConfiguration)
                .build();

//        if(!s3client.doesBucketExistV2(bucketName)) {
//            // Put Warning or error message or create a new bucket based on requirement -
//            // camel automatically creates bucket with public setting if it doesn't exist and credentials have permission
//        }

        return s3client;
    }
}
