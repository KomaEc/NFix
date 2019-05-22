package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.Type;
import groovyjarjarasm.asm.signature.SignatureReader;
import groovyjarjarasm.asm.signature.SignatureVisitor;
import groovyjarjarasm.asm.signature.SignatureWriter;

public abstract class Remapper {
   public String mapDesc(String var1) {
      Type var2 = Type.getType(var1);
      switch(var2.getSort()) {
      case 9:
         String var3 = this.mapDesc(var2.getElementType().getDescriptor());

         for(int var5 = 0; var5 < var2.getDimensions(); ++var5) {
            var3 = '[' + var3;
         }

         return var3;
      case 10:
         String var4 = this.map(var2.getInternalName());
         if (var4 != null) {
            return 'L' + var4 + ';';
         }
      default:
         return var1;
      }
   }

   private Type mapType(Type var1) {
      String var2;
      switch(var1.getSort()) {
      case 9:
         var2 = this.mapDesc(var1.getElementType().getDescriptor());

         for(int var3 = 0; var3 < var1.getDimensions(); ++var3) {
            var2 = '[' + var2;
         }

         return Type.getType(var2);
      case 10:
         var2 = this.map(var1.getInternalName());
         if (var2 != null) {
            return Type.getObjectType(var2);
         }
      default:
         return var1;
      }
   }

   public String mapType(String var1) {
      return var1 == null ? null : this.mapType(Type.getObjectType(var1)).getInternalName();
   }

   public String[] mapTypes(String[] var1) {
      String[] var2 = null;
      boolean var3 = false;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         String var5 = var1[var4];
         String var6 = this.map(var5);
         if (var6 != null && var2 == null) {
            var2 = new String[var1.length];
            if (var4 > 0) {
               System.arraycopy(var1, 0, var2, 0, var4);
            }

            var3 = true;
         }

         if (var3) {
            var2[var4] = var6 == null ? var5 : var6;
         }
      }

      return var3 ? var2 : var1;
   }

   public String mapMethodDesc(String var1) {
      if ("()V".equals(var1)) {
         return var1;
      } else {
         Type[] var2 = Type.getArgumentTypes(var1);
         String var3 = "(";

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3 = var3 + this.mapDesc(var2[var4].getDescriptor());
         }

         Type var5 = Type.getReturnType(var1);
         return var5 == Type.VOID_TYPE ? var3 + ")V" : var3 + ')' + this.mapDesc(var5.getDescriptor());
      }
   }

   public Object mapValue(Object var1) {
      return var1 instanceof Type ? this.mapType((Type)var1) : var1;
   }

   public String mapSignature(String var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         SignatureReader var3 = new SignatureReader(var1);
         SignatureWriter var4 = new SignatureWriter();
         SignatureVisitor var5 = this.createRemappingSignatureAdapter(var4);
         if (var2) {
            var3.acceptType(var5);
         } else {
            var3.accept(var5);
         }

         return var4.toString();
      }
   }

   protected SignatureVisitor createRemappingSignatureAdapter(SignatureVisitor var1) {
      return new RemappingSignatureAdapter(var1, this);
   }

   public String mapMethodName(String var1, String var2, String var3) {
      return var2;
   }

   public String mapFieldName(String var1, String var2, String var3) {
      return var2;
   }

   public String map(String var1) {
      return var1;
   }
}
