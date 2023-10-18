package uk.gov.onsdigital.banner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "uk.gov.onsdigital.banner.service" })
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}