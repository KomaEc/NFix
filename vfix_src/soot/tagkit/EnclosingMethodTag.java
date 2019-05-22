package soot.tagkit;

public class EnclosingMethodTag implements Tag {
   private String enclosingClass;
   private String enclosingMethod;
   private String enclosingMethodSig;

   public EnclosingMethodTag(String c, String m, String s) {
      this.enclosingClass = c;
      this.enclosingMethod = m;
      this.enclosingMethodSig = s;
   }

   public String toString() {
      return "Enclosing Class: " + this.enclosingClass + " Enclosing Method: " + this.enclosingMethod + " Sig: " + this.enclosingMethodSig;
   }

   public String getName() {
      return "EnclosingMethodTag";
   }

   public String getInfo() {
      return "EnclosingMethod";
   }

   public String getEnclosingClass() {
      return this.enclosingClass;
   }

   public String getEnclosingMethod() {
      return this.enclosingMethod;
   }

   public String getEnclosingMethodSig() {
      return this.enclosingMethodSig;
   }

   public byte[] getValue() {
      throw new RuntimeException("EnclosingMethodTag has no value for bytecode");
   }
}
