package com.myorg.awsexperiment.route;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileToS3 extends RouteBuilder {

    @Value("${aws.s3.kmsKey}")
    private String kmsKey;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.clientURI}")
    private String clientURI;

    @Value("${aws.s3.objectName}")
    private String objectName;

    @Value("${exception.retryCount}")
    private String retryCount;

    @Value("${exception.delay}")
    private String delay;

    @Override
    public void configure() {

        onException(AmazonServiceException.class)
                .maximumRedeliveries(retryCount)
                .redeliveryDelay(delay)
                .log(LoggingLevel.ERROR, "Service Exception occurred : ${exception.stacktrace}")
                .handled(true)
                .end();

        onException(AmazonClientException.class)
                .maximumRedeliveries(retryCount)
                .redeliveryDelay(delay)
                .log(LoggingLevel.ERROR, "Client Exception occurred : $simple{exception.stacktrace}")
                .handled(true)
                .end();

        onException(Exception.class)
                .maximumRedeliveries(retryCount)
                .redeliveryDelay(delay)
                .log(LoggingLevel.ERROR, "Exception occurred : ${exception.stacktrace}")
                .handled(true)
                .end();

        from("file:/Users/prashantyadav/git/aws-experiment/src/test/resources?fileName=MyFile.txt")
                //.convertBodyTo(byte[].class) // Not required - Check for retry or other issues String.class
                //.streamCaching()
                // .to(doTransformation)
                .log("Starting")
                .setHeader(S3Constants.CONTENT_LENGTH, simple("${in.header.CamelFileLength}"))
                .setHeader(S3Constants.KEY, constant(objectName)) //Can set CamelFileName or whatever we need
                .to(clientURI)
                .log("File Uploaded");

    }
}
