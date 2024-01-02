FROM eclipse-temurin:17-jre-alpine

RUN addgroup --system banner-group && adduser --system banner-user --ingroup banner-group

RUN mkdir -p "/opt/banner"
RUN chown banner-user:banner-group /opt/banner

WORKDIR "/opt/banner"

COPY target/banner-api.jar .

#RUN chmod 550 /opt/banner/banner-api.jar
RUN chown banner-user:banner-group /opt/banner/banner-api.jar

USER banner-user

CMD ["java", "-jar", "banner-api.jar"]