package bsh.org.objectweb.asm;

import java.lang.reflect.Method;

public class Type {
   public static final int VOID = 0;
   public static final int BOOLEAN = 1;
   public static final int CHAR = 2;
   public static final int BYTE = 3;
   public static final int SHORT = 4;
   public static final int INT = 5;
   public static final int FLOAT = 6;
   public static final int LONG = 7;
   public static final int DOUBLE = 8;
   public static final int ARRAY = 9;
   public static final int OBJECT = 10;
   public static final Type VOID_TYPE = new Type(0);
   public static final Type BOOLEAN_TYPE = new Type(1);
   public static final Type CHAR_TYPE = new Type(2);
   public static final Type BYTE_TYPE = new Type(3);
   public static final Type SHORT_TYPE = new Type(4);
   public static final Type INT_TYPE = new Type(5);
   public static final Type FLOAT_TYPE = new Type(6);
   public static final Type LONG_TYPE = new Type(7);
   public static final Type DOUBLE_TYPE = new Type(8);
   private final int sort;
   private char[] buf;
   private int off;
   private int len;

   private Type(int var1) {
      this.sort = var1;
      this.len = 1;
   }

   private Type(int var1, char[] var2, int var3, int var4) {
      this.sort = var1;
      this.buf = var2;
      this.off = var3;
      this.len = var4;
   }

   public static Type getType(String var0) {
      return getType(var0.toCharArray(), 0);
   }

   public static Type getType(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Integer.TYPE) {
            return INT_TYPE;
         } else if (var0 == Void.TYPE) {
            return VOID_TYPE;
         } else if (var0 == Boolean.TYPE) {
            return BOOLEAN_TYPE;
         } else if (var0 == Byte.TYPE) {
            return BYTE_TYPE;
         } else if (var0 == Character.TYPE) {
            return CHAR_TYPE;
         } else if (var0 == Short.TYPE) {
            return SHORT_TYPE;
         } else if (var0 == Double.TYPE) {
            return DOUBLE_TYPE;
         } else {
            return var0 == Float.TYPE ? FLOAT_TYPE : LONG_TYPE;
         }
      } else {
         return getType(getDescriptor(var0));
      }
   }

   public static Type[] getArgumentTypes(String var0) {
      char[] var1 = var0.toCharArray();
      int var2 = 1;
      int var3 = 0;

      while(true) {
         while(true) {
            char var4 = var1[var2++];
            if (var4 == ')') {
               Type[] var5 = new Type[var3];
               var2 = 1;

               for(var3 = 0; var1[var2] != ')'; ++var3) {
                  var5[var3] = getType(var1, var2);
                  var2 += var5[var3].len;
               }

               return var5;
            }

            if (var4 == 'L') {
               while(var1[var2++] != ';') {
               }

               ++var3;
            } else if (var4 != '[') {
               ++var3;
            }
         }
      }
   }

   public static Type[] getArgumentTypes(Method var0) {
      Class[] var1 = var0.getParameterTypes();
      Type[] var2 = new Type[var1.length];

      for(int var3 = var1.length - 1; var3 >= 0; --var3) {
         var2[var3] = getType(var1[var3]);
      }

      return var2;
   }

   public static Type getReturnType(String var0) {
      char[] var1 = var0.toCharArray();
      return getType(var1, var0.indexOf(41) + 1);
   }

   public static Type getReturnType(Method var0) {
      return getType(var0.getReturnType());
   }

   private static Type getType(char[] var0, int var1) {
      int var2;
      switch(var0[var1]) {
      case 'B':
         return BYTE_TYPE;
      case 'C':
         return CHAR_TYPE;
      case 'D':
         return DOUBLE_TYPE;
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'W':
      case 'X':
      case 'Y':
      default:
         for(var2 = 1; var0[var1 + var2] != ';'; ++var2) {
         }

         return new Type(10, var0, var1, var2 + 1);
      case 'F':
         return FLOAT_TYPE;
      case 'I':
         return INT_TYPE;
      case 'J':
         return LONG_TYPE;
      case 'S':
         return SHORT_TYPE;
      case 'V':
         return VOID_TYPE;
      case 'Z':
         return BOOLEAN_TYPE;
      case '[':
         for(var2 = 1; var0[var1 + var2] == '['; ++var2) {
         }

         if (var0[var1 + var2] == 'L') {
            ++var2;

            while(var0[var1 + var2] != ';') {
               ++var2;
            }
         }

         return new Type(9, var0, var1, var2 + 1);
      }
   }

   public int getSort() {
      return this.sort;
   }

   public int getDimensions() {
      int var1;
      for(var1 = 1; this.buf[this.off + var1] == '['; ++var1) {
      }

      return var1;
   }

   public Type getElementType() {
      return getType(this.buf, this.off + this.getDimensions());
   }

   public String getClassName() {
      return (new String(this.buf, this.off + 1, this.len - 2)).replace('/', '.');
   }

   public String getInternalName() {
      return new String(this.buf, this.off + 1, this.len - 2);
   }

   public String getDescriptor() {
      StringBuffer var1 = new StringBuffer();
      this.getDescriptor(var1);
      return var1.toString();
   }

   public static String getMethodDescriptor(Type var0, Type[] var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append('(');

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var1[var3].getDescriptor(var2);
      }

      var2.append(')');
      var0.getDescriptor(var2);
      return var2.toString();
   }

   private void getDescriptor(StringBuffer var1) {
      switch(this.sort) {
      case 0:
         var1.append('V');
         return;
      case 1:
         var1.append('Z');
         return;
      case 2:
         var1.append('C');
         return;
      case 3:
         var1.append('B');
         return;
      case 4:
         var1.append('S');
         return;
      case 5:
         var1.append('I');
         return;
      case 6:
         var1.append('F');
         return;
      case 7:
         var1.append('J');
         return;
      case 8:
         var1.append('D');
         return;
      default:
         var1.append(this.buf, this.off, this.len);
      }
   }

   public static String getInternalName(Class var0) {
      return var0.getName().replace('.', '/');
   }

   public static String getDescriptor(Class var0) {
      StringBuffer var1 = new StringBuffer();
      getDescriptor(var1, var0);
      return var1.toString();
   }

   public static String getMethodDescriptor(Method var0) {
      Class[] var1 = var0.getParameterTypes();
      StringBuffer var2 = new StringBuffer();
      var2.append('(');

      for(int var3 = 0; var3 < var1.length; ++var3) {
         getDescriptor(var2, var1[var3]);
      }

      var2.append(')');
      getDescriptor(var2, var0.getReturnType());
      return var2.toString();
   }

   private static void getDescriptor(StringBuffer var0, Class var1) {
      Class var2;
      for(var2 = var1; !var2.isPrimitive(); var2 = var2.getComponentType()) {
         if (!var2.isArray()) {
            var0.append('L');
            String var3 = var2.getName();
            int var4 = var3.length();

            for(int var5 = 0; var5 < var4; ++var5) {
               char var6 = var3.charAt(var5);
               var0.append(var6 == '.' ? '/' : var6);
            }

            var0.append(';');
            return;
         }

         var0.append('[');
      }

      char var7;
      if (var2 == Integer.TYPE) {
         var7 = 'I';
      } else if (var2 == Void.TYPE) {
         var7 = 'V';
      } else if (var2 == Boolean.TYPE) {
         var7 = 'Z';
      } else if (var2 == Byte.TYPE) {
         var7 = 'B';
      } else if (var2 == Character.TYPE) {
         var7 = 'C';
      } else if (var2 == Short.TYPE) {
         var7 = 'S';
      } else if (var2 == Double.TYPE) {
         var7 = 'D';
      } else if (var2 == Float.TYPE) {
         var7 = 'F';
      } else {
         var7 = 'J';
      }

      var0.append(var7);
   }

   public int getSize() {
      return this.sort != 7 && this.sort != 8 ? 1 : 2;
   }

   public int getOpcode(int var1) {
      if (var1 != 46 && var1 != 79) {
         switch(this.sort) {
         case 0:
            return var1 + 5;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            return var1;
         case 6:
            return var1 + 2;
         case 7:
            return var1 + 1;
         case 8:
            return var1 + 3;
         default:
            return var1 + 4;
         }
      } else {
         switch(this.sort) {
         case 0:
            return var1 + 5;
         case 1:
         case 3:
            return var1 + 6;
         case 2:
            return var1 + 7;
         case 4:
            return var1 + 8;
         case 5:
            return var1;
         case 6:
            return var1 + 2;
         case 7:
            return var1 + 1;
         case 8:
            return var1 + 3;
         default:
            return var1 + 4;
         }
      }
   }
}
