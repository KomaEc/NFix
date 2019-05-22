package soot.tagkit;

public class ThrowCreatedByCompilerTag implements Tag {
   public String getName() {
      return "ThrowCreatedByCompilerTag";
   }

   public byte[] getValue() {
      throw new RuntimeException("ThrowCreatedByCompilerTag has no value for bytecode");
   }
}
