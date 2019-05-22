package soot.tagkit;

public class SyntheticParamTag implements Tag {
   public String toString() {
      return "SyntheticParamTag";
   }

   public String getName() {
      return "SyntheticParamTag";
   }

   public String getInfo() {
      return "SyntheticParam";
   }

   public byte[] getValue() {
      throw new RuntimeException("SyntheticParamTag has no value for bytecode");
   }
}
