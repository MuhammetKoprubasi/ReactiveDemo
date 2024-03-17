package com.qred.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
@SpringBootApplication
public class QredApplication {

  public static void main(String[] args) {
    SpringApplication.run(QredApplication.class, args);
  }

}
