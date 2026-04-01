package threads;

import java.net.URL;


public class ThreadsApiGetDirPath {
  public static void main(String[] args) {
    final Thread currentThread = Thread.currentThread();
    final ClassLoader classLoader = currentThread.getContextClassLoader();
    final URL resource = classLoader.getResource("");
    final String path = resource.getPath();
    System.out.println(path);
  }
}
