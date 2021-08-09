package com.example.demo;

import com.azure.data.appconfiguration.ConfigurationClient;
import com.azure.data.appconfiguration.models.ConfigurationSetting;
import com.azure.spring.cloud.config.AppConfigurationRefresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.Future;

@Configuration
public class FeatureManagerClient {

  public static final String FEATURE_FLAG_PREFIX = ".appconfig.featureflag/";

  private final ConfigurationClient configurationClient;

  @Autowired
  private AppConfigurationRefresh appConfigurationRefresh;

  @Autowired
  public FeatureManagerClient(ConfigurationClient configurationClient) {
    this.configurationClient = configurationClient;
  }

  /**
   *
   * @param flagName
   * @return
   */
  public boolean getFeatureFlagStatus(final String flagName) {
    boolean retValue = false;

    Map<String, Boolean> featureMap = getFeatureFlags();
    if (!featureMap.isEmpty()) {
      Boolean status = featureMap.get(flagName);
      if(null != status) {
        retValue = status;
      }
    }
    return retValue;
  }

  /**
   * This didn't work for me.
   *
   * @param flagName
   * @param label
   * @return
   */
  public boolean getFeatureFlagStatus(String flagName, String label) {
    // Azure stores Feature Flags as '.appconfig.featureflag/<featureName>' in ConfigurationClient
    var configSetting =
        configurationClient
            .getConfigurationSetting(
                FEATURE_FLAG_PREFIX + flagName,
                label
            );

    var isFeatureFlagEnabled = isFeatureFlagEnabled(configSetting.getValue());
    return isFeatureFlagEnabled;
  }



  /**
   * Does not get the feature flag but the property.
   * @param flagName
   * @return
   */
  public boolean getFeatureFlagStatus2(final String flagName) {
    ConfigurationSetting configurationSetting;

    try {
      configurationSetting =
          this.configurationClient
              .getConfigurationSetting(FEATURE_FLAG_PREFIX + flagName, null);
    } catch (Exception e) {
      System.out.println(e);
    }

    try {
      configurationSetting =
          this.configurationClient
              .getConfigurationSetting(flagName, null);
    } catch (Exception e) {
      System.out.println(e);
    }

    return false;
  }

  /**
   * Retrieves the list of feature flags and poplulates a map with the values.
   * This seems like the only way to actually get the feature flag.
   *
   * This assumes that you want to pull multiple flags and store them in a map.  There
   * Does not seem to be a way to query for individual values
   * @return
   */
  public Map getFeatureFlags() {
    Map featureMap = new HashMap<String, Boolean>();

    Iterator<ConfigurationSetting> iterator =
        this.configurationClient
            .listConfigurationSettings(null)
            .iterator();

    while (iterator.hasNext()) {
      ConfigurationSetting item = iterator.next();
      String key = item.getKey();
      if (key.indexOf(FEATURE_FLAG_PREFIX) >= 0) {
        String featureName = key.substring(FEATURE_FLAG_PREFIX.length());
        boolean enabled = isFeatureFlagEnabled(item.getValue());

        featureMap.put(featureName, enabled);
      }
    }

    return featureMap;
  }

  private boolean isFeatureFlagEnabled(String value) {
    return value.indexOf("\"enabled\":true") >= 0;
  }

  public void myConfigurationRefreshCheck() {
    Future<Boolean> triggeredRefresh = appConfigurationRefresh.refreshConfigurations();
  }
}