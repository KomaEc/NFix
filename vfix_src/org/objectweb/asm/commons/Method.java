package org.objectweb.asm.commons;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.Type;

public class Method {
   private final String name;
   private final String desc;
   private static final Map<String, String> DESCRIPTORS = new HashMap();

   public Method(String name, String desc) {
      this.name = name;
      this.desc = desc;
   }

   public Method(String name, Type returnType, Type[] argumentTypes) {
      this(name, Type.getMethodDescriptor(returnType, argumentTypes));
   }

   public static Method getMethod(java.lang.reflect.Method m) {
      return new Method(m.getName(), Type.getMethodDescriptor(m));
   }

   public static Method getMethod(Constructor<?> c) {
      return new Method("<init>", Type.getConstructorDescriptor(c));
   }

   public static Method getMethod(String method) throws IllegalArgumentException {
      return getMethod(method, false);
   }

   public static Method getMethod(String method, boolean defaultPackage) throws IllegalArgumentException {
      int space = method.indexOf(32);
      int start = method.indexOf(40, space) + 1;
      int end = method.indexOf(41, start);
      if (space != -1 && start != -1 && end != -1) {
         String returnType = method.substring(0, space);
         String methodName = method.substring(space + 1, start - 1).trim();
         StringBuilder sb = new StringBuilder();
         sb.append('(');

         int p;
         do {
            p = method.indexOf(44, start);
            String s;
            if (p == -1) {
               s = map(method.substring(start, end).trim(), defaultPackage);
            } else {
               s = map(method.substring(start, p).trim(), defaultPackage);
               start = p + 1;
            }

            sb.append(s);
         } while(p != -1);

         sb.append(')');
         sb.append(map(returnType, defaultPackage));
         return new Method(methodName, sb.toString());
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static String map(String type, boolean defaultPackage) {
      if ("".equals(type)) {
         return type;
      } else {
         StringBuilder sb = new StringBuilder();
         int index = 0;

         while((index = type.indexOf("[]", index) + 1) > 0) {
            sb.append('[');
         }

         String t = type.substring(0, type.length() - sb.length() * 2);
         String desc = (String)DESCRIPTORS.get(t);
         if (desc != null) {
            sb.append(desc);
         } else {
            sb.append('L');
            if (t.indexOf(46) < 0) {
               if (!defaultPackage) {
                  sb.append("java/lang/");
               }

               sb.append(t);
            } else {
               sb.append(t.replace('.', '/'));
            }

            sb.append(';');
         }

         return sb.toString();
      }
   }

   public String getName() {
      return this.name;
   }

   public String getDescriptor() {
      return this.desc;
   }

   public Type getReturnType() {
      return Type.getReturnType(this.desc);
   }

   public Type[] getArgumentTypes() {
      return Type.getArgumentTypes(this.desc);
   }

   public String toString() {
      return this.name + this.desc;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Method)) {
         return false;
      } else {
         Method other = (Method)o;
         return this.name.equals(other.name) && this.desc.equals(other.desc);
      }
   }

   public int hashCode() {
      return this.name.hashCode() ^ this.desc.hashCode();
   }

   static {
      DESCRIPTORS.put("void", "V");
      DESCRIPTORS.put("byte", "B");
      DESCRIPTORS.put("char", "C");
      DESCRIPTORS.put("double", "D");
      DESCRIPTORS.put("float", "F");
      DESCRIPTORS.put("int", "I");
      DESCRIPTORS.put("long", "J");
      DESCRIPTORS.put("short", "S");
      DESCRIPTORS.put("boolean", "Z");
   }
}
