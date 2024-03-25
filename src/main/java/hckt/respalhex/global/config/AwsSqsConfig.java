package hckt.respalhex.global.config;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.amazonaws.services.sqs.AmazonSQSRequesterClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.AmazonSQSResponderClientBuilder;
import io.awspring.cloud.sqs.config.SqsBootstrapConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.time.Duration;

@Import(SqsBootstrapConfiguration.class)
@Configuration
public class AwsSqsConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String AWS_ACCESS_KEY;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String AWS_SECRET_KEY;

    @Value("${spring.cloud.aws.region.static}")
    private String AWS_REGION;

    // sqs Client For Async
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return AWS_ACCESS_KEY;
                    }

                    @Override
                    public String secretAccessKey() {
                        return AWS_SECRET_KEY;
                    }
                })
                .region(Region.of(AWS_REGION))
                .build();
    }

    // sqs Client For Sync
    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return AWS_ACCESS_KEY;
                    }
                    @Override
                    public String secretAccessKey() {
                        return AWS_SECRET_KEY;
                    }
                })
                .region(Region.of(AWS_REGION))
                .build();
    }

    // Sync Aws Publisher
    @Bean
    public AmazonSQSRequester publisherFactory() {
        return AmazonSQSRequesterClientBuilder.standard()
                .withAmazonSQS(sqsClient())
                .build();
    }

    // Sync Aws Consumer
    @Bean
    public AmazonSQSResponder consumerFactory() {
        return AmazonSQSResponderClientBuilder.standard()
                .withAmazonSQS(sqsClient())
                .build();
    }

    // Async Aws Publisher
    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.newTemplate(sqsAsyncClient());
    }

    // Async Aws Consumer
    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }
}
