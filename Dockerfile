FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine

RUN addgroup -S banner-group && adduser -S banner-user -G banner-group

RUN mkdir -p "/opt/banner"
RUN chown banner-user:banner-group /opt/banner

WORKDIR "/opt/banner"

COPY target/banner-api.jar .

#RUN chmod 550 /opt/banner/banner-api.jar
RUN chown banner-user:banner-group /opt/banner/banner-api.jar

USER banner-user

CMD ["java", "-jar", "banner-api.jar"]