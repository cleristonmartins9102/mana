package reflection;

import java.lang.reflect.*;

public class Reflection {
  public static void main(String[] args) {
    final Product product = new Product();
    try {
      Class<?> clazz = product.getClass();
      Field descriptionField = clazz.getDeclaredField("description");
      System.out.print(descriptionField.get(product));
    } catch (Exception e) {
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
