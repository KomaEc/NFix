package soot.tagkit;

public class SyntheticTag implements Tag {
   public String toString() {
      return "Synthetic";
   }

   public String getName() {
      return "SyntheticTag";
   }

   public String getInfo() {
      return "Synthetic";
   }

   public byte[] getValue() {
      throw new RuntimeException("SyntheticTag has no value for bytecode");
   }
}
