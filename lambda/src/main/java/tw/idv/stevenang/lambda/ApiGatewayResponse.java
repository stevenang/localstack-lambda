package tw.idv.stevenang.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Value
public class ApiGatewayResponse {

    private int statusCode;
    private String body;
    private Map<String, String> headers;
    private boolean isBase64Encoded;

    public static ApiGatewayResponseBuilder builder() {
        return new ApiGatewayResponseBuilder();
    }

    public static class ApiGatewayResponseBuilder {
        private static final Logger LOG = LoggerFactory.getLogger(ApiGatewayResponseBuilder.class);
        private static final ObjectMapper mapper = new ObjectMapper();

        private int statusCode = 200;
        private Map<String, String> headers = new HashMap<>();
        private String rawBody;
        private Object objectBody;
        private byte[] binaryBody;
        private boolean base64Encoded;

        public ApiGatewayResponseBuilder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ApiGatewayResponseBuilder setHeader(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ApiGatewayResponseBuilder setRawBody(String rawBody) {
            this.rawBody = rawBody;
            return this;
        }

        public ApiGatewayResponseBuilder setObjectBody(Object rawBody) {
            this.objectBody = objectBody;
            return this;
        }

        public ApiGatewayResponseBuilder setBinaryBody(byte[] binaryBody) {
            this.binaryBody = binaryBody;
            return this;
        }

        public ApiGatewayResponseBuilder setBase64Encoded(boolean base64Encoded) {
            this.base64Encoded = base64Encoded;
            return this;
        }

        public ApiGatewayResponse build() {
            String body = null;
            if (StringUtils.isNotBlank(this.rawBody)) {
                body = this.rawBody;
            } else if (objectBody != null) {
                try {
                    body = mapper.writeValueAsString(objectBody);
                } catch (JsonProcessingException exception) {
                    LOG.error("Failed to serialized body object");
                    throw new RuntimeException(exception);
                }
            } else if (binaryBody != null) {
                body = new String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8);
            }

            return new ApiGatewayResponse(statusCode, body, headers, base64Encoded);
        }
    }
}

