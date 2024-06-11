package ro.foodx.backend.configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${custom.aws.s3.endpoint}")
    private String s3Endpoint;

    @Value("${custom.aws.ses.endpoint}")
    private String sesEndpoint;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .endpointOverride(URI.create(s3Endpoint))
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }

    @Bean
    public SesClient sesClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return SesClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .endpointOverride(URI.create(sesEndpoint))
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }
}
