package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.bytecode.FrameOptions;
import com.gzoltar.shaded.org.pitest.classinfo.ComputeClassWriter;
import com.gzoltar.shaded.org.pitest.classpath.ClassloaderByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sun.pitest.CodeCoverageStore;

public class CoverageTransformer implements ClassFileTransformer {
   private final Predicate<String> filter;
   private final Map<String, String> computeCache = new ConcurrentHashMap();

   public CoverageTransformer(Predicate<String> filter) {
      this.filter = filter;
   }

   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
      boolean include = this.shouldInclude(className);
      if (include) {
         try {
            return this.transformBytes(loader, className, classfileBuffer);
         } catch (RuntimeException var8) {
            System.err.println("RuntimeException while transforming  " + className);
            var8.printStackTrace();
            throw var8;
         }
      } else {
         return null;
      }
   }

   private byte[] transformBytes(ClassLoader loader, String className, byte[] classfileBuffer) {
      ClassReader reader = new ClassReader(classfileBuffer);
      ClassWriter writer = new ComputeClassWriter(new ClassloaderByteArraySource(loader), this.computeCache, FrameOptions.pickFlags(classfileBuffer));
      int id = CodeCoverageStore.registerClass(className);
      reader.accept(new CoverageClassVisitor(id, writer), 8);
      return writer.toByteArray();
   }

   private boolean shouldInclude(String className) {
      return (Boolean)this.filter.apply(className);
   }
}
