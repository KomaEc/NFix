package bsh;

import java.lang.reflect.Array;

class BSHType extends SimpleNode implements BshClassManager.Listener {
   private Class baseType;
   private int arrayDims;
   private Class type;
   String descriptor;

   BSHType(int var1) {
      super(var1);
   }

   public void addArrayDimension() {
      ++this.arrayDims;
   }

   SimpleNode getTypeNode() {
      return (SimpleNode)this.jjtGetChild(0);
   }

   public String getTypeDescriptor(CallStack var1, Interpreter var2, String var3) {
      if (this.descriptor != null) {
         return this.descriptor;
      } else {
         SimpleNode var4 = this.getTypeNode();
         String var5;
         if (var4 instanceof BSHPrimitiveType) {
            var5 = getTypeDescriptor(((BSHPrimitiveType)var4).type);
         } else {
            String var6 = ((BSHAmbiguousName)var4).text;
            BshClassManager var7 = var2.getClassManager();
            String var8 = var7.getClassBeingDefined(var6);
            Class var9 = null;
            if (var8 == null) {
               try {
                  var9 = ((BSHAmbiguousName)var4).toClass(var1, var2);
               } catch (EvalError var11) {
               }
            } else {
               var6 = var8;
            }

            if (var9 != null) {
               var5 = getTypeDescriptor(var9);
            } else if (var3 != null && !Name.isCompound(var6)) {
               var5 = "L" + var3.replace('.', '/') + "/" + var6 + ";";
            } else {
               var5 = "L" + var6.replace('.', '/') + ";";
            }
         }

         for(int var12 = 0; var12 < this.arrayDims; ++var12) {
            var5 = "[" + var5;
         }

         this.descriptor = var5;
         return var5;
      }
   }

   public Class getType(CallStack var1, Interpreter var2) throws EvalError {
      if (this.type != null) {
         return this.type;
      } else {
         SimpleNode var3 = this.getTypeNode();
         if (var3 instanceof BSHPrimitiveType) {
            this.baseType = ((BSHPrimitiveType)var3).getType();
         } else {
            this.baseType = ((BSHAmbiguousName)var3).toClass(var1, var2);
         }

         if (this.arrayDims > 0) {
            try {
               int[] var4 = new int[this.arrayDims];
               Object var5 = Array.newInstance(this.baseType, var4);
               this.type = var5.getClass();
            } catch (Exception var6) {
               throw new EvalError("Couldn't construct array type", this, var1);
            }
         } else {
            this.type = this.baseType;
         }

         var2.getClassManager().addListener(this);
         return this.type;
      }
   }

   public Class getBaseType() {
      return this.baseType;
   }

   public int getArrayDims() {
      return this.arrayDims;
   }

   public void classLoaderChanged() {
      this.type = null;
      this.baseType = null;
   }

   public static String getTypeDescriptor(Class var0) {
      if (var0 == Boolean.TYPE) {
         return "Z";
      } else if (var0 == Character.TYPE) {
         return "C";
      } else if (var0 == Byte.TYPE) {
         return "B";
      } else if (var0 == Short.TYPE) {
         return "S";
      } else if (var0 == Integer.TYPE) {
         return "I";
      } else if (var0 == Long.TYPE) {
         return "J";
      } else if (var0 == Float.TYPE) {
         return "F";
      } else if (var0 == Double.TYPE) {
         return "D";
      } else if (var0 == Void.TYPE) {
         return "V";
      } else {
         String var1 = var0.getName().replace('.', '/');
         return !var1.startsWith("[") && !var1.endsWith(";") ? "L" + var1.replace('.', '/') + ";" : var1;
      }
   }
}
