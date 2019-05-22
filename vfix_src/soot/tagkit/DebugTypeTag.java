package soot.tagkit;

public class DebugTypeTag extends SignatureTag {
   public DebugTypeTag(String signature) {
      super(signature);
   }

   public String toString() {
      return "DebugType: " + this.getSignature();
   }

   public String getName() {
      return "DebugTypeTag";
   }

   public String getInfo() {
      return "DebugType";
   }

   public byte[] getValue() {
      throw new RuntimeException("DebugTypeTag has no value for bytecode");
   }
}
