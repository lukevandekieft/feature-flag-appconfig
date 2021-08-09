package com.example.demo;

import com.azure.data.appconfiguration.ConfigurationClient;
import com.azure.data.appconfiguration.ConfigurationClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationClientProvider {

  @Autowired
  ConfigurationClientProvider() { }

  @Bean
  public ConfigurationClient createConfigurationClient() {
    String connectionString = System.getenv("APP_CONFIGURATION_CONNECTION_STRING");
    return new ConfigurationClientBuilder().connectionString(connectionString).buildClient();
  }
}