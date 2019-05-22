package com.gzoltar.shaded.javassist.bytecode.annotation;

import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import com.gzoltar.shaded.javassist.bytecode.Descriptor;
import java.io.IOException;
import java.lang.reflect.Method;

public abstract class MemberValue {
   ConstPool cp;
   char tag;

   MemberValue(char tag, ConstPool cp) {
      this.cp = cp;
      this.tag = tag;
   }

   abstract Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException;

   abstract Class getType(ClassLoader cl) throws ClassNotFoundException;

   static Class loadClass(ClassLoader cl, String classname) throws ClassNotFoundException, NoSuchClassError {
      try {
         return Class.forName(convertFromArray(classname), true, cl);
      } catch (LinkageError var3) {
         throw new NoSuchClassError(classname, var3);
      }
   }

   private static String convertFromArray(String classname) {
      int index = classname.indexOf("[]");
      if (index == -1) {
         return classname;
      } else {
         String rawType = classname.substring(0, index);

         StringBuffer sb;
         for(sb = new StringBuffer(Descriptor.of(rawType)); index != -1; index = classname.indexOf("[]", index + 1)) {
            sb.insert(0, "[");
         }

         return sb.toString().replace('/', '.');
      }
   }

   public abstract void accept(MemberValueVisitor visitor);

   public abstract void write(AnnotationsWriter w) throws IOException;
}
