package soot.tagkit;

public class DeprecatedTag implements Tag {
   public String toString() {
      return "Deprecated";
   }

   public String getName() {
      return "DeprecatedTag";
   }

   public String getInfo() {
      return "Deprecated";
   }

   public byte[] getValue() {
      throw new RuntimeException("DeprecatedTag has no value for bytecode");
   }
}
