package com.huawei.dli.sdk.demo.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DemoConf {
  private Properties properties;
  private static DemoConf instance;

  private DemoConf() {
    properties = new Properties();
    try {
      properties.load(new FileInputStream(System.getProperty("user.dir") + "/resources/conf/demo.conf"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static DemoConf getInstance() {
    if (instance == null) {
      instance = new DemoConf();
    }
    return instance;
  }

  public String get(String key) {
    return properties.getProperty(key);
  }
}
