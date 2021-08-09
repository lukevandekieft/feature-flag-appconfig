package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class handles the config as Azure Properties (as opposed to Feature Flag, the on/off toggle).
 *
 * Steps (Within Azure Portal):
 *
 *  1.)  Goto your App Configuration service
 *  2.)  Under Operations-> Configuration Explorer (left menu)
 *  3.)  Press the "+ Create" button
 *  4.)  Choose "Key-value"
 *  5.)  For each property you want to track add a key/value pair
 *          ex:  key = "/application/config.Beta" value = "On"
 *  6.)  These values (assuming they have the "config." prefix will be automatically
 *       mapped to the fields within this class.  This class expects the following key/value
 *       pairs in Azure App Config (each of which will be mapped to the member fields below):
 *
 *          /application/config.Beta
 *          /application/config.message"
 *          /application/config.sentinal"
 *  7.)  These fields will ONLY be mapped to their values set in Azure at application startup unless
 *       you configure monitoring
 *       (see https://docs.microsoft.com/en-us/azure/azure-app-configuration/enable-dynamic-configuration-java-spring-app).
 */
@Configuration
@ConfigurationProperties(prefix = "config")
public class FeatureFlagConfiguration {
  private String beta;
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getBeta() {
    return beta;
  }

  public void setBeta(String beta) {
    this.beta = beta;
  }
}