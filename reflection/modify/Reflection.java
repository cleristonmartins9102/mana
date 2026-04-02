package reflection.modify;

import java.lang.reflect.*;

public class Reflection {
  public static void main(String[] args) {
    final Product product = new Product();
    try {
      Class<?> clazz = product.getClass();
      Field externalIdField = clazz.getDeclaredField("externalId");
      externalIdField.setAccessible(true);
      externalIdField.set(product, "hash13");
      System.out.print(externalIdField.get(product));
    } catch (Exception e) {
      System.out.print(e);
    }
 }
}

class Product {
  private String id = "10";
  private String externalId = "hash12";
  String description = "any description";
  
  public String getDescription() {
    return description;
  }
}
