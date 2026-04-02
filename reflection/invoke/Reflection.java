package reflection.invoke;

import java.lang.reflect.*;

public class Reflection {
  public static void main(String[] args) {
    final Product product = new Product();
    try {
      Class<?> clazz = product.getClass();
      Method method = clazz.getDeclaredMethod("getDescription");
      System.out.print(method.invoke(product));
    } catch (Exception e) {
      System.out.print(e);
    }
 }
}

class Product {
  private String id = "10";
  String description = "any description";
  
  public String getDescription() {
    return description;
  }
}
