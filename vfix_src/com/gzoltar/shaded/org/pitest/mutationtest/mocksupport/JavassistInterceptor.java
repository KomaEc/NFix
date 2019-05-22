package com.gzoltar.shaded.org.pitest.mutationtest.mocksupport;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutant;
import com.gzoltar.shaded.org.pitest.reflection.Reflection;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class JavassistInterceptor {
   private static Mutant mutant;

   private JavassistInterceptor() {
   }

   public static InputStream openClassfile(Object classPath, String name) {
      if (isMutatedClass(name)) {
         ByteArrayInputStream bais = new ByteArrayInputStream(mutant.getBytes());
         return bais;
      } else {
         return returnNormalBytes(classPath, name);
      }
   }

   private static InputStream returnNormalBytes(Object classPath, String name) {
      try {
         return (InputStream)Reflection.publicMethod(classPath.getClass(), "openClassfile").invoke(classPath, name);
      } catch (Exception var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   private static boolean isMutatedClass(String name) {
      return mutant != null && mutant.getDetails().getClassName().equals(ClassName.fromString(name));
   }

   public static void setMutant(Mutant newMutant) {
      mutant = newMutant;
   }
}
