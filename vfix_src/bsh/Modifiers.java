package bsh;

import java.io.Serializable;
import java.util.Hashtable;

public class Modifiers implements Serializable {
   public static final int CLASS = 0;
   public static final int METHOD = 1;
   public static final int FIELD = 2;
   Hashtable modifiers;

   public void addModifier(int var1, String var2) {
      if (this.modifiers == null) {
         this.modifiers = new Hashtable();
      }

      Object var3 = this.modifiers.put(var2, Void.TYPE);
      if (var3 != null) {
         throw new IllegalStateException("Duplicate modifier: " + var2);
      } else {
         int var4 = 0;
         if (this.hasModifier("private")) {
            ++var4;
         }

         if (this.hasModifier("protected")) {
            ++var4;
         }

         if (this.hasModifier("public")) {
            ++var4;
         }

         if (var4 > 1) {
            throw new IllegalStateException("public/private/protected cannot be used in combination.");
         } else {
            switch(var1) {
            case 0:
               this.validateForClass();
               break;
            case 1:
               this.validateForMethod();
               break;
            case 2:
               this.validateForField();
            }

         }
      }
   }

   public boolean hasModifier(String var1) {
      if (this.modifiers == null) {
         this.modifiers = new Hashtable();
      }

      return this.modifiers.get(var1) != null;
   }

   private void validateForMethod() {
      this.insureNo("volatile", "Method");
      this.insureNo("transient", "Method");
   }

   private void validateForField() {
      this.insureNo("synchronized", "Variable");
      this.insureNo("native", "Variable");
      this.insureNo("abstract", "Variable");
   }

   private void validateForClass() {
      this.validateForMethod();
      this.insureNo("native", "Class");
      this.insureNo("synchronized", "Class");
   }

   private void insureNo(String var1, String var2) {
      if (this.hasModifier(var1)) {
         throw new IllegalStateException(var2 + " cannot be declared '" + var1 + "'");
      }
   }

   public String toString() {
      return "Modifiers: " + this.modifiers;
   }
}
