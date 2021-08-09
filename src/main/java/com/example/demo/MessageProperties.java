
package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * This class was part of the instructions provided in the trainin but was never used.
 */
@Configuration
@ConfigurationProperties(prefix = "config2")
public class MessageProperties {
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}