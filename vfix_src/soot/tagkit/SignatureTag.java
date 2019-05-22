package soot.tagkit;

public class SignatureTag implements Tag {
   private String signature;

   public SignatureTag(String signature) {
      this.signature = signature;
   }

   public String toString() {
      return "Signature: " + this.signature;
   }

   public String getName() {
      return "SignatureTag";
   }

   public String getInfo() {
      return "Signature";
   }

   public String getSignature() {
      return this.signature;
   }

   public byte[] getValue() {
      throw new RuntimeException("SignatureTag has no value for bytecode");
   }
}
