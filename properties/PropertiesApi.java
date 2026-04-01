package properties;

import java.io.FileInputStream;
import java.util.Properties;

class PropertiesApi {
  public static void main(String[] args) {
    final Properties envProps = new Properties();
    final String path = PropertiesApi.class.getClassLoader().getResource("").getPath();
    try {
      envProps.load(new FileInputStream(path + "/dev.env"));
      envProps.stringPropertyNames().forEach(key -> {
        System.out.println(key + "=" + envProps.getProperty(key));
      });
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}