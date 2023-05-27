From openjdk:11

ARG APPLICATION_NAME_ARGS="data-engine-lambda"
ENV APPLICATION_NAME=${APPLICATION_NAME_ARGS}

ARG ENV_ARG="dev"
ENV ENVIRONMENT=${ENV_ARG}

WORKDIR /opt/data-engine

COPY ./entrypoint.sh ./entrypoint.sh
COPY ./target/data-engine-lambda-*.jar ./data-engine-lambda.jar

ENTRYPOINT ["./entrypoint.sh"]