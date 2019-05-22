package com.gzoltar.shaded.org.pitest.mutationtest.mocksupport;

import com.gzoltar.shaded.org.pitest.bytecode.FrameOptions;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class BendJavassistToMyWillTransformer implements ClassFileTransformer {
   private final Predicate<String> filter;

   public BendJavassistToMyWillTransformer(Predicate<String> filter) {
      this.filter = filter;
   }

   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
      if (this.shouldInclude(className)) {
         ClassReader reader = new ClassReader(classfileBuffer);
         ClassWriter writer = new ClassWriter(FrameOptions.pickFlags(classfileBuffer));
         reader.accept(new JavassistInputStreamInterceptorAdapater(writer), 8);
         return writer.toByteArray();
      } else {
         return null;
      }
   }

   private boolean shouldInclude(String className) {
      return (Boolean)this.filter.apply(className);
   }
}
