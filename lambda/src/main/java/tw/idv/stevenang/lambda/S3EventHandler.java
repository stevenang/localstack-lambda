package tw.idv.stevenang.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class S3EventHandler implements RequestHandler<S3Event, ApiGatewayResponse> {


    @Override
    public ApiGatewayResponse handleRequest(S3Event s3Event, Context context) {

        if (s3Event.getRecords() != null) {
            try {
                S3EventNotification.S3EventNotificationRecord record = s3Event.getRecords().get(0);
                S3EventNotification.S3Entity entity = record.getS3();

                log.info("Bucket: {}", entity.getBucket());
                log.info("Key: {}", entity.getObject().getKey());

                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setRawBody(String.format("Received %s/%s", entity.getBucket(), entity.getObject().getKey()))
                        .build();

            } catch (Exception exception) {
                log.error("{}", exception.getMessage());
                throw exception;
            }
        } else {
            log.warn("S3Event received with no records");
            return ApiGatewayResponse.builder()
                    .setStatusCode(400)
                    .setRawBody("S3Event with no record")
                    .build();
        }
    }
}
