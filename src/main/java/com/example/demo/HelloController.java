package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@ConfigurationProperties("controller")
public class HelloController {

  private FeatureManagerClient featureManagerClient;

  FeatureFlagConfiguration featureFlagConfiguration;

  public HelloController(FeatureManagerClient featureManagerClient, FeatureFlagConfiguration featureFlagConfiguration) {
    this.featureManagerClient = featureManagerClient;
    this.featureFlagConfiguration = featureFlagConfiguration;
  }

  @GetMapping("/welcome")
  public String mainWithParam(Model model) {
    //This code as written in the instruction does not work (as far as I can tell).
    //model.addAttribute("Beta", featureManager.isEnabledAsync("featureManagement.Beta").block());


    //OPTION 1:  To do feature flags using the "Configuration explorer" within Azure App Configuration service.
    this.featureManagerClient.myConfigurationRefreshCheck();
    boolean betaFlagAsProperty =
        "On".equals(featureFlagConfiguration.getBeta());

    //OPTION 2:  To do feature flags using the "Feature manager" (enabled/disabled toggle) within Azure App Configuration service.
    boolean betaFlagAsFeatureFlag =
        this.featureManagerClient
            .getFeatureFlagStatus(
                "Beta");

    model
        .addAttribute("Beta", betaFlagAsFeatureFlag);
    model
        .addAttribute("Message", featureFlagConfiguration.getMessage());

    return "welcome";
  }
}